package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.constant.Constants;
import com.services.ServiceProvider.entity.RefreshToken;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.exception.LogoutFailedException;
import com.services.ServiceProvider.exception.TokenNotCreatedException;
import com.services.ServiceProvider.exception.UserNotFoundException;
import com.services.ServiceProvider.repository.RefreshTokenRepository;
import com.services.ServiceProvider.repository.UserRepository;
import com.services.ServiceProvider.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    @Override
    public RefreshToken createRefreshToken(String username) {
        try {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                throw new UserNotFoundException(410,username,Constants.RESPONSE.get(410));
            }

            RefreshToken refreshTokenOb = refreshTokenRepository.findByUser(user);

            if (refreshTokenOb == null) {
                refreshTokenOb = RefreshToken.builder()
                        .refreshToken(UUID.randomUUID().toString())
                        .expiry(Instant.now().plusMillis(Constants.REFRESH_TOKEN_VALIDITY))
                        .user(user)
                        .build();
            } else {
                refreshTokenOb.setExpiry(Instant.now().plusMillis(Constants.REFRESH_TOKEN_VALIDITY));
            }

            refreshTokenRepository.save(refreshTokenOb);

            return refreshTokenOb;

        } catch (TokenNotCreatedException e) {
            throw new TokenNotCreatedException(433, Constants.RESPONSE.get(123));
        }
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) throws Exception {
        try {
            RefreshToken refreshTokenOb = refreshTokenRepository
                    .findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new IllegalArgumentException("Given token does not exist!"));

            if (refreshTokenOb.getExpiry().isBefore(Instant.now())) {
                refreshTokenRepository.delete(refreshTokenOb);
                throw new IllegalStateException("Refresh token is expired");
            }

            return refreshTokenOb;

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;

        } catch (Exception e) {
            throw new Exception("Failed to verify refresh token", e);
        }
    }


    @Override
    @Transactional
    public void logout(String userName) {
        try {
            // Fetch the user
            User user = userRepository.findByUsername(userName).orElse(null);
            if (user == null) {
                throw new UserNotFoundException(410,userName,Constants.RESPONSE.get(410));
            }

            // Fetch the refresh token associated with the user
            RefreshToken refreshTokenOb = refreshTokenRepository.findByUser(user);
            if (refreshTokenOb == null) {
                return;
            }

            String refreshToken = refreshTokenOb.getRefreshToken();

            // Delete if token exists
            if (refreshToken != null) {
                refreshTokenRepository.deleteByRefreshToken(refreshToken);
            }

        } catch (IllegalArgumentException e) {
            logger.info("Validation error during logout");
            throw e;

        } catch (Exception e) {
            logger.info("Unexpected error during logout for user");
            throw new LogoutFailedException(454,Constants.RESPONSE.get(454));
        }
    }

}