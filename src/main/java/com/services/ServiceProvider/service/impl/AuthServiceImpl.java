package com.services.ServiceProvider.service.impl;

import com.sendgrid.Response;
import com.services.ServiceProvider.constant.Constants;
import com.services.ServiceProvider.dto.AuthResponseDto;
import com.services.ServiceProvider.dto.ForgotPasswordTokenData;
import com.services.ServiceProvider.entity.RefreshToken;
import com.services.ServiceProvider.entity.Role;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.exception.TokenException;
import com.services.ServiceProvider.payload.request.PasswordRequest;
import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.repository.UserRepository;
import com.services.ServiceProvider.security.CustomUserDetails;
import com.services.ServiceProvider.security.JwtHelper;
import com.services.ServiceProvider.service.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtHelper helper;
    private final UserDetailsService userDetailsService;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;

    // In-memory token store
    private final Map<String, ForgotPasswordTokenData> tokenStore = new ConcurrentHashMap<>();

    public AuthServiceImpl(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, UserService userService, JwtHelper helper, UserDetailsService userDetailsService, RoleService roleService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.helper = helper;
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
        this.refreshTokenService = refreshTokenService;
    }

    public ApiResponse processForgotPassword(String email) {
        User user = userService.getUserByUserName(email);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);

        // Store token with associated email
        tokenStore.put(token, new ForgotPasswordTokenData(email, expiry));

        // Send email
        Response response = emailService.sendResetLink(email, token);
        if(response.getStatusCode() >= 200 && response.getStatusCode() < 300){
            return ApiResponse.builder().status(true).code(131).build();
        }

        return ApiResponse.builder().status(false).code(132).build();
    }

    public ApiResponse resetPassword(String token, PasswordRequest request) {
        ForgotPasswordTokenData data = tokenStore.get(token);
        if (data == null || data.getExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenException(446, Constants.RESPONSE.get(446));
        }
        ApiResponse apiResponse = userService.updatePassword(request, data.getEmail());

        // Invalidate token after use
        tokenStore.remove(token);
        return apiResponse;
    }

    @Override
    public AuthResponseDto generateAuthResponse(String userName) {
        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        // Generate JWT token
        String jwtToken = helper.generateToken(userDetails);

        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        // Get user roles
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Set<Role> roles = customUserDetails.getUser().getRoles();

        // Optionally convert role names to string set
        Set<String> roleNames = roleService.getUserRoleNames(userName);

        // Create a response
        AuthResponseDto dto = new AuthResponseDto();
        dto.setJwtToken(jwtToken);
        dto.setRefreshToken(refreshToken.getRefreshToken());
        dto.setRoles(roleNames);

        return dto;
    }
}