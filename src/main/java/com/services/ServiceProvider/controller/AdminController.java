package com.services.ServiceProvider.controller;

import com.services.ServiceProvider.dto.UserResponseDto;
import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.payload.response.BookingResponse;
import com.services.ServiceProvider.service.impl.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam(defaultValue = "0") int page) {
        Page<UserResponseDto> users = adminService.getAllUsers(page, 5); // Fixed page size = 5

        ApiResponse response = ApiResponse.builder()
                .status(true)
                .data(users)
                .message("Users fetched successfully")
                .build();

        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiResponse> getAllBookings(
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<BookingResponse> bookings = adminService.getAllBookings(page, 5);

        ApiResponse response = ApiResponse.builder()
                .status(true)
                .data(bookings.getContent())
                .message("Bookings fetched successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}