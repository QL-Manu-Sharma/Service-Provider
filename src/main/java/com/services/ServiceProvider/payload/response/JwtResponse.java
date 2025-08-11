package com.services.ServiceProvider.payload.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class JwtResponse {
    private String jwtToken;
    private Set<String> roles;
    private String refreshToken;
    private Boolean isEmailVerified;

}