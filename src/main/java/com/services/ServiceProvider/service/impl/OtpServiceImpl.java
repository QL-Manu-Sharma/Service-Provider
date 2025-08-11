package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.constant.Constants;
import com.services.ServiceProvider.entity.OtpEntity;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.exception.OtpException;
import com.services.ServiceProvider.exception.UserNotFoundException;
import com.services.ServiceProvider.repository.OtpRepository;
import com.services.ServiceProvider.repository.UserRepository;
import com.services.ServiceProvider.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;

    @Override
    public boolean validateOtp(String email, Long code) {
        Boolean isOtpExpired = false;
        // Fetch the latest OTP entry for the given email
        OtpEntity otp = otpRepository.findTopByEmailOrderByCreatedAtDesc(email);

        // Fetch user and validate
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new UserNotFoundException(410, email, Constants.RESPONSE.get(410)));

        // Check if OTP exists, matches the provided code, and is not expired
        if (otp == null || !otp.getCode().equals(code)) {
            throw new OtpException(449,Constants.RESPONSE.get(449));
        }
        if(otp.getExpiry().isBefore(LocalDateTime.now())){
            throw new OtpException(450,Constants.RESPONSE.get(450));
        }



        if (!Boolean.TRUE.equals(user.getIsEmailVerified())) {
            user.setIsEmailVerified(true);
            userRepository.save(user);
            return true;
        }

        // Email was already verified, optionally return false or throw
        return false;
    }

    @Override
    public OtpEntity saveOtp(String email, Long otp) {
        OtpEntity otpEntity = otpRepository.findTopByEmailOrderByCreatedAtDesc(email);

        if (otpEntity == null) {
            otpEntity = new OtpEntity();
            otpEntity.setEmail(email);
        }

        otpEntity.setCode(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiry(otpEntity.getCreatedAt().plusMinutes(5));

        return otpRepository.save(otpEntity);
    }

}