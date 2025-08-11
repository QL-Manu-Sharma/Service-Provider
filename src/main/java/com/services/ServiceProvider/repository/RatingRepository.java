package com.services.ServiceProvider.repository;

import com.services.ServiceProvider.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProviderId(Long providerId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.provider.id = :providerId")
    Double findAverageRating(Long providerId);
}