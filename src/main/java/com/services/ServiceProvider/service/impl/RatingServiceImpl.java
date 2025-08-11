package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.constant.BookingStatus;
import com.services.ServiceProvider.entity.Booking;
import com.services.ServiceProvider.entity.ProviderProfile;
import com.services.ServiceProvider.entity.Rating;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.payload.response.RatingResponse;
import com.services.ServiceProvider.repository.BookingRepository;
import com.services.ServiceProvider.repository.ProviderProfileRepository;
import com.services.ServiceProvider.repository.RatingRepository;
import com.services.ServiceProvider.service.RatingService;
import com.services.ServiceProvider.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepo;
    private final BookingRepository bookingRepo;
    private final ProviderProfileRepository providerRepo;
    private final UserService userService;

    @Override
    public RatingResponse submitRating(String username, Long bookingId, int stars, String comment) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow();
        User user = userService.getUserByUserName(username);
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Not authorized to rate this booking.");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot rate an incomplete booking.");
        }
        Rating savedRating = saveRating(booking,stars,comment);

        // Update avg rating
        Double avg = ratingRepo.findAverageRating(booking.getProvider().getId());
        ProviderProfile provider = booking.getProvider();
        provider.setAvgRating(avg);
        providerRepo.save(provider);

        return mapperForRatingResponse(savedRating);
    }


    @Override
    public List<RatingResponse> getRatingsForProvider(Long providerId) {
        List<RatingResponse> ratingResponses = new ArrayList<>();
        List<Rating> ratings = ratingRepo.findByProviderId(providerId);
        for(Rating rating : ratings){
            ratingResponses.add(mapperForRatingResponse(rating));
        }
        return ratingResponses;
    }

    public static RatingResponse mapperForRatingResponse(Rating rating) {
        if (rating == null) {
            return null;
        }

        return RatingResponse.builder()
                .booking(rating.getBooking())
                .user(rating.getUser())
                .provider(rating.getProvider())
                .rating(rating.getRating())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .build();
    }

    public Rating saveRating(Booking booking, int stars, String comment){
        Rating rating = new Rating();
        rating.setBooking(booking);
        rating.setUser(booking.getUser());
        rating.setProvider(booking.getProvider());
        rating.setRating(stars);
        rating.setComment(comment);
        rating.setCreatedAt(LocalDateTime.now());

        Rating saved = ratingRepo.save(rating);
        return saved;
    }
}