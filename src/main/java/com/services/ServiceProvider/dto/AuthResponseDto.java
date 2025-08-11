package com.services.ServiceProvider.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AuthResponseDto {

    private String jwtToken;
    private String refreshToken;
    private Set<String> roles;


}