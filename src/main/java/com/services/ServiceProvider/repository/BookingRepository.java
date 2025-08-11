package com.services.ServiceProvider.repository;

import com.services.ServiceProvider.entity.Booking;
import com.services.ServiceProvider.entity.ProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.provider.id = :providerId AND b.date = :date " +
           "AND b.startTime < :endTime AND b.endTime > :startTime")
    List<Booking> findOverlappingBookings(Long providerId, LocalDate date,
                                          LocalTime startTime, LocalTime endTime);

    List<Booking> findByUserId(Long userId);
    List<Booking> findByProviderId(Long providerId);

    @Query("SELECT b.provider FROM Booking b WHERE b.id = :bookingId")
    ProviderProfile findProviderByBookingId(@Param("bookingId") Long bookingId);
}