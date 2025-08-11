package com.services.ServiceProvider.repository;

import com.services.ServiceProvider.entity.ProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {
    Optional<ProviderProfile> findByUserId(Long userId);
    Optional<ProviderProfile> findByUserUsername(String username);
}