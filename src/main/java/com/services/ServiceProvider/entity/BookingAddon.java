package com.services.ServiceProvider.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class BookingAddon {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Booking booking;

    @ManyToOne
    private ServiceAddon addon;
}