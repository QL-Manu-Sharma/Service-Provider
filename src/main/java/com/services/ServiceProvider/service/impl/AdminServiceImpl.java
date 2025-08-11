package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.constant.Constants;
import com.services.ServiceProvider.dto.UserResponseDto;
import com.services.ServiceProvider.entity.Booking;
import com.services.ServiceProvider.entity.ProviderProfile;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.exception.ProviderNotFoundException;
import com.services.ServiceProvider.exception.UserNotFoundException;
import com.services.ServiceProvider.payload.response.BookingResponse;
import com.services.ServiceProvider.payload.response.ProviderProfileResponse;
import com.services.ServiceProvider.repository.BookingRepository;
import com.services.ServiceProvider.repository.ProviderProfileRepository;
import com.services.ServiceProvider.repository.UserRepository;
import com.services.ServiceProvider.service.AdminService;
import com.services.ServiceProvider.service.BookingService;
import com.services.ServiceProvider.service.ProviderService;
import com.services.ServiceProvider.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepo;
    private final ProviderProfileRepository providerRepo;
    private final BookingRepository bookingRepo;
    private final ProviderService providerService;
    private final BookingService bookingService;
    private final UserService userService;

    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> userPage = userRepo.findAll(pageable);
        return userPage.map(userService::userMapperToResponseDto);
    }

    @Override
    public void blockUser(Long userId, boolean block) {
        User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException(410,null, Constants.RESPONSE.get(410)));
        user.setIsBlocked(block);
        userRepo.save(user);
    }


    @Override
    public void blockProvider(Long providerId, boolean block) {
        ProviderProfile profile = providerRepo.findById(providerId).orElseThrow(()-> new ProviderNotFoundException(453,Constants.RESPONSE.get(453)));
        User user = profile.getUser();
        user.setIsBlocked(block);
        userRepo.save(user);
    }

    @Override
    public Page<BookingResponse> getAllBookings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Booking> bookingPage = bookingRepo.findAll(pageable);

        return bookingPage.map(bookingService::bookingMapperToResponse);
    }
}