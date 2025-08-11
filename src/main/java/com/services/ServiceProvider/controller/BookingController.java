package com.services.ServiceProvider.controller;

import com.services.ServiceProvider.constant.BookingStatus;
import com.services.ServiceProvider.payload.request.BookingRequest;
import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.payload.response.BookingResponse;
import com.services.ServiceProvider.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createBooking(@RequestBody BookingRequest request,
                                                                        Principal principal) {
        BookingResponse booking = bookingService.createBooking(
                principal.getName(),
                request.getProviderId(),
                request.getServiceId(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getAddonIds()
        );
        return ResponseEntity.ok(ApiResponse.builder().data(booking).message("Booking done").build());
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getUserBookings(Principal principal) {
        return ResponseEntity.ok(ApiResponse.builder().data(bookingService.getBookingsForUser(principal.getName())).build());
    }

    @GetMapping(value = "/provider", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getProviderBookings(Principal principal) {
        return ResponseEntity.ok(ApiResponse.builder().data(bookingService.getBookingsForProvider(principal.getName())).build());
    }


    @PatchMapping(value = "/{bookingId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateBookingStatus(@PathVariable Long bookingId,
                                                 @RequestParam BookingStatus status) {
        bookingService.updateStatus(bookingId, status);
        return ResponseEntity.ok(ApiResponse.builder().message("status updated successfully!").build());
    }
}