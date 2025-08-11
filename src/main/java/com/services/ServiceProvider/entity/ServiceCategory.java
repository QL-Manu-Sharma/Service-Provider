package com.services.ServiceProvider.entity;

import com.services.ServiceProvider.constant.PricingType;
import com.services.ServiceProvider.constant.ServiceCategoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class ServiceCategory {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ServiceCategoryType categoryName;

    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    private PricingType pricingType;

    @DecimalMin(value = "0.0", inclusive = true, message = "Additional price must be 0 or greater")
    private double basePrice;
}
