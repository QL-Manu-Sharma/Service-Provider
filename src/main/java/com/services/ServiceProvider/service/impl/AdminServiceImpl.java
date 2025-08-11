package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.dto.UserResponseDto;
import com.services.ServiceProvider.entity.Booking;
import com.services.ServiceProvider.entity.ProviderProfile;
import com.services.ServiceProvider.entity.User;
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
    public List<UserResponseDto> getAllUsers() {
        List<UserResponseDto> responseDtoList = new ArrayList<>();
        List<User> users =  userRepo.findAll();
        for(User user : users){
            responseDtoList.add(userService.userMapperToResponseDto(user));
        }
        return responseDtoList;
    }

    @Override
    public void blockUser(Long userId, boolean block) {
        User user = userRepo.findById(userId).orElseThrow();
        user.setIsBlocked(block);
        userRepo.save(user);
    }


    @Override
    public void blockProvider(Long providerId, boolean block) {
        ProviderProfile profile = providerRepo.findById(providerId).orElseThrow();
        User user = profile.getUser();
        user.setIsBlocked(block);
        userRepo.save(user);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        List<BookingResponse> bookingResponses = new ArrayList<>();
        List<Booking> lists = bookingRepo.findAll();
        for(Booking booking : lists){
            bookingResponses.add(bookingService.bookingMapperToResponse(booking));
        }
        return bookingResponses;
    }
}