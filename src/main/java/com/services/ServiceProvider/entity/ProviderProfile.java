package com.services.ServiceProvider.entity;

import com.services.ServiceProvider.constant.ProviderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;



@Entity
@Data
public class ProviderProfile {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    private String services; // comma-separated string like "Plumbing, Electrical"

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City name can't be longer than 100 characters")
    private String city;

    private double hourlyRate;

    private double avgRating;

    @Enumerated(EnumType.STRING)
    private ProviderStatus status;
}