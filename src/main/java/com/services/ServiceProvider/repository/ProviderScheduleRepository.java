package com.services.ServiceProvider.repository;

import com.services.ServiceProvider.entity.ProviderSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderScheduleRepository extends JpaRepository<ProviderSchedule, Long> {
    List<ProviderSchedule> findByProviderId(Long providerId);
}