package com.services.ServiceProvider.controller;

import com.services.ServiceProvider.payload.request.ProviderRequest;
import com.services.ServiceProvider.payload.request.ProviderScheduleRequest;
import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.payload.response.ProviderProfileResponse;
import com.services.ServiceProvider.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping(value = "/profile",consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createOrUpdateProfile(@RequestBody ProviderRequest profile,
                                                                         Principal principal) {
        ProviderProfileResponse response = providerService.createOrUpdateProfile(principal.getName(), profile);
        return ResponseEntity.ok(ApiResponse.builder().data(response).message("Profile managed successfully!").build());
    }

    @PostMapping(value = "/schedule",consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> setSchedule(@RequestBody List<ProviderScheduleRequest> schedules,
                                         Principal principal) {
        ProviderProfileResponse profile = providerService.getProvider(principal.getName());
        providerService.setSchedule(profile.getId(), schedules);
        return ResponseEntity.ok(ApiResponse.builder().message("Schedule set successfully!").build());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllProviders() {
        return ResponseEntity.ok(ApiResponse.builder().data(providerService.getAllProviders()).build());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProvider(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.getProviderById(id));
    }
}