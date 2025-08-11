package com.services.ServiceProvider.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingRequest {

    @NotNull(message = "Provider ID is required")
    private Long providerId;
    @NotNull(message = "Service ID is required")
    private Long serviceId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<Long> addonIds;
}