package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.entity.ServiceAddon;
import com.services.ServiceProvider.entity.ServiceCategory;
import com.services.ServiceProvider.payload.response.ServiceCategoryResponse;
import com.services.ServiceProvider.repository.ServiceAddonRepository;
import com.services.ServiceProvider.repository.ServiceCategoryRepository;
import com.services.ServiceProvider.service.ServiceCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceCategoryRepository serviceCategoryRepository;
    private final ServiceAddonRepository addonRepo;


    @Override
    public List<ServiceCategoryResponse> getServicesByCategory() {
        List<ServiceCategoryResponse> categoryResponses = new ArrayList<>();
        List<ServiceCategory> category =  serviceCategoryRepository.findAll();
        for(ServiceCategory serviceCategory : category){
            categoryResponses.add(ServiceCategoryResponseMapper(serviceCategory));
        }
        return categoryResponses;
    }

    public static ServiceCategoryResponse ServiceCategoryResponseMapper(ServiceCategory serviceCategory) {
        if (serviceCategory == null) {
            return null;
        }

        ServiceCategoryResponse response = new ServiceCategoryResponse();
        response.setCategoryName(serviceCategory.getCategoryName());
        response.setDescription(serviceCategory.getDescription());
        response.setPricingType(serviceCategory.getPricingType());
        response.setBasePrice(serviceCategory.getBasePrice());

        return response;
    }

    @Override
    public List<ServiceAddon> getAddonsByService(Long serviceId) {
        return addonRepo.findByServiceCategoryId(serviceId);
    }

}