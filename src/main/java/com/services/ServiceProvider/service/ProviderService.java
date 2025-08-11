package com.services.ServiceProvider.service;

import com.services.ServiceProvider.entity.ProviderProfile;
import com.services.ServiceProvider.entity.ProviderSchedule;
import com.services.ServiceProvider.payload.request.ProviderRequest;
import com.services.ServiceProvider.payload.request.ProviderScheduleRequest;
import com.services.ServiceProvider.payload.response.ProviderProfileResponse;

import java.util.List;

public interface ProviderService {
    ProviderProfileResponse createOrUpdateProfile(String username, ProviderRequest profileData);

    void setSchedule(Long providerId, List<ProviderScheduleRequest> scheduleList);

    Double getAverageRating(Long providerId);

    List<ProviderProfileResponse> getAllProviders();

    ProviderProfileResponse getProvider(String username);


    ProviderProfileResponse getProviderById(Long id);


    ProviderSchedule providerRequestMapperToSchedule(ProviderScheduleRequest request);

    ProviderProfileResponse providerMapperToResponse(ProviderProfile providerProfile);
}
