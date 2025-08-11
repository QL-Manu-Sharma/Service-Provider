package com.services.ServiceProvider.repository;

import com.services.ServiceProvider.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    OtpEntity findTopByEmailOrderByCreatedAtDesc(String email);

}