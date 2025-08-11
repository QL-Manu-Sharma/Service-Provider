package com.services.ServiceProvider.service;

import com.services.ServiceProvider.dto.UserResponseDto;
import com.services.ServiceProvider.entity.Booking;
import com.services.ServiceProvider.entity.ProviderProfile;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.payload.response.BookingResponse;
import com.services.ServiceProvider.payload.response.ProviderProfileResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    Page<UserResponseDto> getAllUsers(int pageNumber , int pageSize);

    void blockUser(Long userId, boolean block);

    void blockProvider(Long providerId, boolean block);

    Page<BookingResponse> getAllBookings(int pageNumber, int pageSize);
}
