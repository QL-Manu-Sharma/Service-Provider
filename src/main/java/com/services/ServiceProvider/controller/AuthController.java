package com.services.ServiceProvider.controller;

import com.services.ServiceProvider.dto.AuthResponseDto;
import com.services.ServiceProvider.entity.RefreshToken;
import com.services.ServiceProvider.entity.Role;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.payload.request.*;
import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.payload.response.JwtResponse;
import com.services.ServiceProvider.security.CustomUserDetails;
import com.services.ServiceProvider.security.JwtHelper;
import com.services.ServiceProvider.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper helper;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private final RoleService roleService;
    private final OtpService otpService;
    private final EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtHelper helper, UserService userService, RefreshTokenService refreshTokenService, AuthService authService, RoleService roleService, OtpService otpService, EmailService emailService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.helper = helper;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.authService = authService;
        this.roleService = roleService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/login" ,consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse servletResponse){
        boolean isValid = helper.doAuthenticatePassword(request.getUsername(), request.getPassword());
        if(isValid) {
            AuthResponseDto authResponse  =  authService.generateAuthResponse(request.getUsername());
            JwtResponse response = JwtResponse.builder().jwtToken(authResponse.getJwtToken()).refreshToken(authResponse.getRefreshToken())
                    .roles((Set<String>) authResponse.getRoles()).isEmailVerified(true).build();
            return ResponseEntity.ok(ApiResponse.builder().status(true).code(112).data(response).build());
        }
        return ResponseEntity.badRequest().body(ApiResponse.builder().status(false).code(430).build());
    }


    @PostMapping(value = "/refresh",consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> refreshJwtToken(@RequestBody RefreshTokenRequest request) throws Exception {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Set<Role> roles = customUserDetails.getUser().getRoles();
        Set<String> roleNames = roleService.getUserRoleNames(user.getUsername());
        String token = this.helper.generateToken(userDetails);
        JwtResponse response = JwtResponse.builder().refreshToken(refreshToken.getRefreshToken()).jwtToken(token).roles(roleNames).build();
        return ResponseEntity.ok(ApiResponse.builder().status(true).code(126).data(response).build());
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String jweToken) {
        String userName = helper.getUserNameFromToken(jweToken);
        refreshTokenService.logout(userName);
        return ResponseEntity.ok(ApiResponse.builder().status(true).code(120).build());
    }

    @PostMapping(value = "/forgot-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email) {
        ApiResponse apiResponse = authService.processForgotPassword(email);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String token, @RequestBody PasswordRequest request) {
        ApiResponse apiResponse = authService.resetPassword(token, request);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value = "/verifyOtp", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody @Valid SignupRequest request, HttpServletResponse servletResponse) {
        boolean isValid = helper.doAuthenticateOtp(request.getUserName(), request.getOtp());
        if (isValid) {
            AuthResponseDto authResponse = authService.generateAuthResponse(request.getUserName());
            JwtResponse response = JwtResponse.builder().jwtToken(authResponse.getJwtToken()).refreshToken(authResponse.getRefreshToken())
                    .roles(authResponse.getRoles()).isEmailVerified(true).build();
            return ResponseEntity.ok(ApiResponse.builder().status(true).code(111).data(response).build());
        }
        return ResponseEntity.badRequest().body(ApiResponse.builder().status(false).code(429).build());

    }
    @PostMapping(value = "/signup",consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> sendOtp(@RequestBody @Valid EmailRequest request) {
        Long otp = emailService.generateOtp();

        boolean success = emailService.sendOtpEmail(request.getEmail(), otp, request.getRole());

        if (success) {

            otpService.saveOtp(request.getEmail(), otp);
            return ResponseEntity.ok(ApiResponse.builder().status(true).code(110).build());
        } else {
            if(emailService.isEmailVerified(request.getEmail())){
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("isEmailVerified", true);
                ApiResponse apiResponse = ApiResponse.builder().status(false).data(responseData).code(122).build();
                return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getHttpStatusCode()));
            }
            ApiResponse apiResponse = ApiResponse.builder().status(false).data("username : "+ request.getEmail()).code(432).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getHttpStatusCode()));
        }
    }

}