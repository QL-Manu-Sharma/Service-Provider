package com.services.ServiceProvider.repository;

import com.services.ServiceProvider.entity.ServiceAddon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceAddonRepository extends JpaRepository<ServiceAddon, Long> {
    List<ServiceAddon> findByServiceCategoryId(Long serviceId);
}