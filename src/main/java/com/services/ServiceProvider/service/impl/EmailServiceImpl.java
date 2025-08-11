package com.services.ServiceProvider.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.services.ServiceProvider.constant.Constants;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.exception.RequestLimitException;
import com.services.ServiceProvider.exception.UserExistsException;
import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.repository.OtpRepository;
import com.services.ServiceProvider.repository.UserRepository;
import com.services.ServiceProvider.security.JwtHelper;
import com.services.ServiceProvider.service.EmailService;
import com.services.ServiceProvider.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final OtpRepository otpRepository;
    private final JwtHelper helper;
    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    private final Map<String, Instant> tokenStore = new ConcurrentHashMap<>();
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> lastRequestTime = new ConcurrentHashMap<>();

    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Override
    public Long generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return (long) otp;
    }

    @Override
    public boolean sendOtpEmail(String toEmail, Long otp, String role) {
        User user = userRepository.findByUsername(toEmail).orElse(null);

        // If user exists and is already verified, return false
        if (user != null && Boolean.TRUE.equals(user.getIsEmailVerified())) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        int count = requestCounts.getOrDefault(toEmail, 0);
        long lastTime = lastRequestTime.getOrDefault(toEmail, 0L);

        // Too many requests in short time
        if (count >= Constants.OTP_MAX_REQUESTS_IN_ONE_DAY && (currentTime - lastTime) < Constants.OTP_RESEND_WAIT_MILLIS_AFTER_MAX_REQUESTS) {
            throw new RequestLimitException(451,Constants.RESPONSE.get(451));
        }

        // Enforce wait between individual resend requests
        if ((currentTime - lastTime) < Constants.OTP_RESEND_WAIT_MILLIS) {
            return false;
        }

        // Reset count if full wait time passed
        if ((currentTime - lastTime) >= Constants.OTP_RESEND_WAIT_MILLIS_AFTER_MAX_REQUESTS) {
            requestCounts.put(toEmail, 1);
        } else {
            requestCounts.put(toEmail, count + 1);
        }
        lastRequestTime.put(toEmail, currentTime);

        // Send the OTP email
        String subject =  "Your OTP Code";
        Content content = new Content("text/plain", "Your OTP is: " + otp);

        Response response = sendEmail(toEmail,subject,content);

        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            // Create dummy user if not found
            if (user == null) {
                User newUser = new User();
                newUser.setUsername(toEmail);
                newUser.setIsDummyUser(true);
                userRepository.save(newUser);
                newUser.setRoles(userService.assignRoleToUser(toEmail,role));
            }
            return true;
        } else {
            return false;
        }
    }

    public Boolean isEmailVerified(String email) {
        User user = userRepository.findByUsername(email).orElse(null);
        if (user == null || !Boolean.TRUE.equals(user.getIsEmailVerified())) {
            return false;
        }
        return true;
    }

    @Override
    public Response sendEmail(String toEmail, String subject, Content content){
        try {  Email from = new Email(fromEmail);
            String emailSubject = subject;
            Email to = new Email(toEmail);
            Content eamilContent = content;
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            return sg.api(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponse sendDashboardAccessEmail(String toEmail, String role) throws IOException {
        User existingUser = userRepository.findByUsername(toEmail).orElse(null);
        if(existingUser!=null){
            throw new UserExistsException(452,Constants.RESPONSE.get(452));
        }
        String token = generateInvitationToken();
        String emailSubject = "Access Your Dashboard";
        String accessUrl = baseUrl + "?token=" + token;
        String contentText = "Click the link below to access your dashboard:\n\n" + accessUrl;
        Content emailContent = new Content("text/plain", contentText);
        Response  response = sendEmail(toEmail,emailSubject,emailContent);
        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300){
            if(existingUser == null){
                User newUser = new User();
                newUser.setUsername(toEmail);
                userRepository.save(newUser);
                userService.assignRoleToUser(toEmail,role);
            }
        }
        ApiResponse apiResponse = new ApiResponse();
        if(response.getStatusCode() >= 200 && response.getStatusCode() < 300){
            return  apiResponse = ApiResponse.builder().code(127).status(true).data(token).build();
        }
        else{
            return apiResponse = ApiResponse.builder().status(false).code(435).build();
        }
    }

    @Override
    public String generateInvitationToken() {
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        Instant expiryTime = Instant.now().plusSeconds(Constants.INVITATION_TOKEN_VALIDITY);
        tokenStore.put(token, expiryTime);
        return token;
    }

    @Override
    public boolean isInvitationTokenValid(String token) {
        Instant expiry = tokenStore.get(token);
        if (expiry == null) return false;
        if (Instant.now().isAfter(expiry)) {
            tokenStore.remove(token); // clean up expired token
            return false;
        }
        return true;
    }

    public Response sendResetLink(String toEmail, String token) {
        String resetUrl = "http://localhost:8080/api/v1/auth/reset-password?token=" + token;
        String subject = "Reset your password";
        Content content = new Content("text/plain", "Click the link to reset password: " + resetUrl);

        try {
            Response response = sendEmail(toEmail, subject, content);
            return response;
        } catch (Exception e) {
            // Log the error (using your preferred logging framework)
            logger.info("Failed to send password reset email");
            // Optionally, return a Response object indicating failure
            Response errorResponse = new Response();
            errorResponse.setStatusCode(500); // or appropriate status
            errorResponse.setBody("Failed to send reset link. Please try again later.");
            return errorResponse;
        }
    }


}