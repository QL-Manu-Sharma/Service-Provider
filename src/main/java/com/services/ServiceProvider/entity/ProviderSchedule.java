package com.services.ServiceProvider.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data
public class ProviderSchedule {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private ProviderProfile provider;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
}