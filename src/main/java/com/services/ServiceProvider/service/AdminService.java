package com.services.ServiceProvider.service;

import com.services.ServiceProvider.dto.UserResponseDto;
import com.services.ServiceProvider.entity.Booking;
import com.services.ServiceProvider.entity.ProviderProfile;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.payload.response.BookingResponse;
import com.services.ServiceProvider.payload.response.ProviderProfileResponse;

import java.util.List;

public interface AdminService {
    List<UserResponseDto> getAllUsers();

    void blockUser(Long userId, boolean block);

    void blockProvider(Long providerId, boolean block);

    List<BookingResponse> getAllBookings();
}
