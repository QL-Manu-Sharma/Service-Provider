package com.services.ServiceProvider.payload.response;

import com.services.ServiceProvider.constant.BookingStatus;
import com.services.ServiceProvider.entity.ProviderProfile;
import com.services.ServiceProvider.entity.ServiceCategory;
import com.services.ServiceProvider.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class BookingResponse {
    private User user;
    private ProviderProfile provider;
    private ServiceCategory serviceCategory;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private LocalDateTime createdAt;
}
