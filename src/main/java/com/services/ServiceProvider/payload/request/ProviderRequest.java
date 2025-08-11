package com.services.ServiceProvider.payload.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProviderRequest {


    private String services;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City name can't be longer than 100 characters")
    private String city;

    @Min(value = 0, message = "Hourly rate must be at least 0")
    @Max(value = 10000, message = "Hourly rate must be less than or equal to 10,000")
    private double hourlyRate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Average rating must be at least 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Average rating must not exceed 5.0")
    private double avgRating;
}
