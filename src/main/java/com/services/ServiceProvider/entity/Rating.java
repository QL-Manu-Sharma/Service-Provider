package com.services.ServiceProvider.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Rating {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Booking booking;

    @ManyToOne
    private User user;

    @ManyToOne
    private ProviderProfile provider;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private int rating;

    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String comment;

    private LocalDateTime createdAt;
}