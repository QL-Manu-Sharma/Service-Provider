package com.services.ServiceProvider.controller;

import com.services.ServiceProvider.entity.ServiceAddon;
import com.services.ServiceProvider.payload.response.ServiceCategoryResponse;
import com.services.ServiceProvider.service.ServiceCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class ServiceCatalogController {

    private final ServiceCatalogService catalogService;

    @GetMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceCategoryResponse>> getServices() {
        return ResponseEntity.ok(catalogService.getServicesByCategory());
    }

    @GetMapping(value = "/addons", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceAddon>> getAddons(@RequestParam Long serviceId) {
        return ResponseEntity.ok(catalogService.getAddonsByService(serviceId));
    }
}