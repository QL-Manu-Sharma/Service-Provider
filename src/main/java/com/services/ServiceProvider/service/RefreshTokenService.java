package com.services.ServiceProvider.service;

import com.services.ServiceProvider.entity.RefreshToken;

public interface RefreshTokenService {


     void logout(String userName);
     RefreshToken verifyRefreshToken(String refreshToken) throws Exception;;
     RefreshToken createRefreshToken(String username);
}