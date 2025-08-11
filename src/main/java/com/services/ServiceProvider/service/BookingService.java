package com.services.ServiceProvider.service;

import com.services.ServiceProvider.constant.BookingStatus;
import com.services.ServiceProvider.entity.Booking;
import com.services.ServiceProvider.payload.response.BookingResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(String username, Long providerId, Long serviceId,
                                  LocalDate date, LocalTime startTime, LocalTime endTime,
                                  List<Long> addonIds);

    void updateStatus(Long bookingId, BookingStatus status);

    List<BookingResponse> getBookingsForUser(String username);

    List<BookingResponse> getBookingsForProvider(String providerEmail);

    BookingResponse bookingMapperToResponse(Booking booking);
}
