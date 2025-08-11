package com.services.ServiceProvider.controller;

import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> submitRating(@RequestParam Long bookingId,
                                                       @RequestParam int rating,
                                                       @RequestParam(required = false) String comment,
                                                       Principal principal) {
        return ResponseEntity.ok(ApiResponse.builder().data(ratingService.submitRating(principal.getName(), bookingId, rating, comment)).status(true).build());
    }

    @GetMapping(value = "/provider/{providerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProviderRatings(@PathVariable Long providerId) {
        return ResponseEntity.ok(ratingService.getRatingsForProvider(providerId));
    }
}