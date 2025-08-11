package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.constant.BookingStatus;
import com.services.ServiceProvider.constant.Constants;
import com.services.ServiceProvider.entity.*;
import com.services.ServiceProvider.exception.UserBookingException;
import com.services.ServiceProvider.exception.UserNotFoundException;
import com.services.ServiceProvider.payload.response.BookingResponse;
import com.services.ServiceProvider.repository.*;
import com.services.ServiceProvider.service.BookingService;
import com.services.ServiceProvider.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.services.ServiceProvider.constant.BookingStatus.ACCEPTED;
import static com.services.ServiceProvider.constant.ProviderStatus.AVAILABLE;
import static com.services.ServiceProvider.constant.ProviderStatus.UNAVAILABLE;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final ProviderProfileRepository providerRepo;
    private final UserRepository userRepo;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final UserService userService;
    private final ServiceAddonRepository serviceAddonRepository;
    private final BookingAddonRepository bookingAddonRepository;

    private Logger logger = LoggerFactory.getLogger(BookingService.class);


    @Override
    public BookingResponse createBooking(String username, Long providerId, Long serviceId,
                                         LocalDate date, LocalTime startTime, LocalTime endTime,
                                         List<Long> addonIds) {

        // Check provider availability
        List<Booking> overlaps = bookingRepo.findOverlappingBookings(providerId, date, startTime, endTime);
        if (!overlaps.isEmpty()) {
            throw new IllegalStateException("Provider is already booked for that time.");
        }
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(410,username, Constants.RESPONSE.get(410)));
        Booking booking = new Booking();
        if(user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("USER"))) {

            booking.setUser(user);
            booking.setProvider(providerRepo.findById(providerId).orElseThrow());
            booking.setServiceCategory(serviceCategoryRepository.findById(serviceId).orElseThrow());
            booking.setDate(date);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setStatus(BookingStatus.REQUESTED);
            booking.setCreatedAt(LocalDateTime.now());
        }
        else{
            throw new UserBookingException(447,Constants.RESPONSE.get(447));
        }
        Booking savedBooking = bookingRepo.save(booking);

        // Save selected add-ons
        if (addonIds != null && !addonIds.isEmpty()) {
            for (Long addonId : addonIds) {
                ServiceAddon addon = serviceAddonRepository.findById(addonId).orElseThrow();
                BookingAddon bookingAddon = new BookingAddon();
                bookingAddon.setBooking(savedBooking);
                bookingAddon.setAddon(addon);
                bookingAddonRepository.save(bookingAddon);
            }
        }


        // Notification (dummy)
        logger.info("Notification: Booking request sent to provider");
        return bookingMapperToResponse(savedBooking);
    }

    @Override
    public void updateStatus(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow();
        booking.setStatus(status);
        ProviderProfile profile = bookingRepo.findProviderByBookingId(bookingId);
        bookingRepo.save(booking);
        if(booking.getStatus().equals(ACCEPTED)){
            profile.setStatus(UNAVAILABLE);
        }
        else{
            profile.setStatus(AVAILABLE);
        }
        providerRepo.save(profile);

        // Notification
        logger.info("Booking status changed");
    }

    @Override
    public List<BookingResponse> getBookingsForUser(String username) {
        List<BookingResponse> responses = new ArrayList<>();
        User user= userService.getUserByUserName(username);
        List<Booking> bookings= bookingRepo.findByUserId(user.getId());
        for(Booking b : bookings){
            responses.add(bookingMapperToResponse(b));
        }
        return responses;
    }

    @Override
    public List<BookingResponse> getBookingsForProvider(String providerEmail) {
        List<BookingResponse> responseList = new ArrayList<>();
        Optional<ProviderProfile> profile = providerRepo.findByUserUsername(providerEmail);
        List<Booking> bookingList =  bookingRepo.findByProviderId(profile.get().getId());
        for(Booking booking : bookingList){
            responseList.add(bookingMapperToResponse(booking));
        }
        return responseList;
    }

    @Override
    public BookingResponse bookingMapperToResponse(Booking booking) {
        if (booking == null) {
            return null;
        }

        return BookingResponse.builder()
                .user(booking.getUser())
                .provider(booking.getProvider())
                .serviceCategory(booking.getServiceCategory())
                .date(booking.getDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}