package com.services.ServiceProvider.payload.response;

import com.services.ServiceProvider.constant.PricingType;
import com.services.ServiceProvider.constant.ServiceCategoryType;
import lombok.Data;

@Data
public class ServiceCategoryResponse {

    private ServiceCategoryType categoryName;
    private String description;
    private PricingType pricingType;
    private double basePrice;
}
