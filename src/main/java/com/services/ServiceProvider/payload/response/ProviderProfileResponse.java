package com.services.ServiceProvider.payload.response;

import com.services.ServiceProvider.constant.ProviderStatus;
import com.services.ServiceProvider.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderProfileResponse {
    private Long id;
    private User user;
    private String services; // comma-separated string like "Plumbing, Electrical"
    private String city;
    private double hourlyRate;
    private double avgRating;
    private ProviderStatus status;

}
