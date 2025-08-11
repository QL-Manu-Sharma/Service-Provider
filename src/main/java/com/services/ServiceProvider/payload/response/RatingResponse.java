package com.services.ServiceProvider.payload.response;

import com.services.ServiceProvider.entity.Booking;
import com.services.ServiceProvider.entity.ProviderProfile;
import com.services.ServiceProvider.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RatingResponse {

    private Booking booking;
    private User user;
    private ProviderProfile provider;
    private int rating; // 1-5
    private String comment;
    private LocalDateTime createdAt;
}
