package com.services.ServiceProvider.service;

import com.sendgrid.Response;
import com.sendgrid.helpers.mail.objects.Content;
import com.services.ServiceProvider.payload.response.ApiResponse;

import java.io.IOException;

public interface EmailService {

     Long generateOtp();
     boolean sendOtpEmail(String toEmail, Long otp, String role);
     Boolean isEmailVerified(String email);

     Response sendEmail(String toEmail, String subject, Content content);

     ApiResponse sendDashboardAccessEmail(String toEmail, String role) throws IOException;

     String generateInvitationToken();

     boolean isInvitationTokenValid(String token);


     Response sendResetLink(String toEmail, String token);
}