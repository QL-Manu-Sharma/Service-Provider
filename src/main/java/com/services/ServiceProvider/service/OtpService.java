package com.services.ServiceProvider.service;

import com.services.ServiceProvider.entity.OtpEntity;

public interface OtpService {


     OtpEntity saveOtp(String email, Long otp);
     boolean validateOtp(String email, Long code);
}