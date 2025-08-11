package com.services.ServiceProvider.service;

import com.services.ServiceProvider.payload.response.RatingResponse;

import java.util.List;

public interface RatingService {
    RatingResponse submitRating(String username, Long bookingId, int stars, String comment);

    List<RatingResponse> getRatingsForProvider(Long providerId);
}
