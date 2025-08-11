package com.services.ServiceProvider.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false)
    private String email;

    @NotNull(message = "OTP code is required")
    @Min(value = 100000, message = "OTP code must be at least 6 digits")
    @Max(value = 999999, message = "OTP code must be at most 6 digits")
    @Column(nullable = false)
    private Long code;

    @NotNull(message = "Expiry time is required")
    @Column(nullable = false)
    private LocalDateTime expiry;

    @NotNull(message = "Created time is required")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}