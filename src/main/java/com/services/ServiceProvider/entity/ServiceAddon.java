package com.services.ServiceProvider.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class ServiceAddon {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @DecimalMin(value = "0.0", inclusive = true, message = "Additional price must be 0 or greater")
    private double additionalPrice;

    @ManyToOne
    private ServiceCategory serviceCategory;
}