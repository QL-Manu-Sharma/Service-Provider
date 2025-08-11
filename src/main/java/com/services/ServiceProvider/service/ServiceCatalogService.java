package com.services.ServiceProvider.service;

import com.services.ServiceProvider.entity.ServiceAddon;
import com.services.ServiceProvider.entity.ServiceCategory;
import com.services.ServiceProvider.payload.response.ServiceCategoryResponse;

import java.util.List;

public interface ServiceCatalogService {


    List<ServiceCategoryResponse> getServicesByCategory();

    List<ServiceAddon> getAddonsByService(Long serviceId);
}
