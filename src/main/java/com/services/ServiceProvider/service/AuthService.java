package com.services.ServiceProvider.service;

import com.services.ServiceProvider.dto.AuthResponseDto;
import com.services.ServiceProvider.payload.request.PasswordRequest;
import com.services.ServiceProvider.payload.response.ApiResponse;

public interface AuthService {
     ApiResponse processForgotPassword(String email);
     ApiResponse resetPassword(String token, PasswordRequest request);
     AuthResponseDto generateAuthResponse(String userName);
}