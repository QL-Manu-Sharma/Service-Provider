package com.services.ServiceProvider.payload.request;

import com.services.ServiceProvider.entity.ProviderProfile;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class ProviderScheduleRequest {

    private ProviderProfile provider;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
}
