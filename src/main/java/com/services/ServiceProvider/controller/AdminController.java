package com.services.ServiceProvider.controller;

import com.services.ServiceProvider.dto.UserResponseDto;
import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.payload.response.BookingResponse;
import com.services.ServiceProvider.service.impl.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminServiceImpl adminService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserResponseDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.builder().data(users).status(true).build());
    }

    @PatchMapping(value = "/block/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> blockUser(@PathVariable Long userId, @RequestParam boolean block) {
        adminService.blockUser(userId, block);
        return  ResponseEntity.ok(ApiResponse.builder().status(true).message("User blocked successfully").build());
    }

    @PatchMapping(value = "/block/providers/{providerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> blockProvider(@PathVariable Long providerId, @RequestParam boolean block) {
        adminService.blockProvider(providerId, block);
        return ResponseEntity.ok(ApiResponse.builder().message("Provider blocked successfully").build());
    }

    @GetMapping(value = "/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllBookings() {
        List<BookingResponse> bookings = adminService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.builder().data(bookings).status(true).build());
    }
}