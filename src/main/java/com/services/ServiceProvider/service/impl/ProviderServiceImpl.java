package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.constant.Constants;
import com.services.ServiceProvider.entity.ProviderProfile;
import com.services.ServiceProvider.entity.ProviderSchedule;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.exception.ProviderNotFoundException;
import com.services.ServiceProvider.exception.UserNotFoundException;
import com.services.ServiceProvider.payload.request.ProviderRequest;
import com.services.ServiceProvider.payload.request.ProviderScheduleRequest;
import com.services.ServiceProvider.payload.response.ProviderProfileResponse;
import com.services.ServiceProvider.repository.ProviderProfileRepository;
import com.services.ServiceProvider.repository.ProviderScheduleRepository;
import com.services.ServiceProvider.repository.RatingRepository;
import com.services.ServiceProvider.repository.UserRepository;
import com.services.ServiceProvider.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderProfileRepository providerRepo;
    private final ProviderScheduleRepository scheduleRepo;
    private final RatingRepository ratingRepo;
    private final UserRepository userRepository;

    @Override
    public ProviderProfileResponse createOrUpdateProfile(String username, ProviderRequest profileData) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException(410,null,Constants.RESPONSE.get(410)));
        if(user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("PROVIDER"))){
            ProviderProfile profile = providerRepo.findByUserId(user.getId())
                    .orElse(new ProviderProfile());
            profile.setUser(new User(user));
            profile.setCity(profileData.getCity());
            profile.setHourlyRate(profileData.getHourlyRate());
            profile.setServices(profileData.getServices());
            providerRepo.save(profile);
            return providerMapperToResponse(profile);
        }
        throw new ProviderNotFoundException(453,Constants.RESPONSE.get(453));
    }

    @Override
    public void setSchedule(Long providerId, List<ProviderScheduleRequest> scheduleList) {
        // Remove old schedule
        scheduleRepo.deleteAll(scheduleRepo.findByProviderId(providerId));
        for (ProviderScheduleRequest s : scheduleList) {
            s.setProvider(providerRepo.findById(providerId).orElseThrow());
            ProviderSchedule profile = providerRequestMapperToSchedule(s);
            scheduleRepo.save(profile);
        }
    }

    @Override
    public Double getAverageRating(Long providerId) {
        return ratingRepo.findAverageRating(providerId);
    }

    @Override
    public List<ProviderProfileResponse> getAllProviders() {
        List<ProviderProfileResponse> responses = new ArrayList<>();
         List<ProviderProfile> providers = providerRepo.findAll();
         for(ProviderProfile p : providers) {
             responses.add(providerMapperToResponse(p));
         }
         return responses;
    }

    @Override
    public ProviderProfileResponse getProvider(String username) {
        ProviderProfile profile = providerRepo.findByUserUsername(username).orElseThrow(()->  new ProviderNotFoundException(453,Constants.RESPONSE.get(453)));
        return providerMapperToResponse(profile);


    }

    @Override
    public ProviderProfileResponse getProviderById(Long id){
        Optional<ProviderProfile> profile = providerRepo.findById(id);
        return providerMapperToResponse(profile.get());
    }

    @Override
    public ProviderSchedule providerRequestMapperToSchedule(ProviderScheduleRequest request) {
        ProviderSchedule schedule = new ProviderSchedule();

        schedule.setProvider(request.getProvider());
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setBreakStartTime(request.getBreakStartTime());
        schedule.setBreakEndTime(request.getBreakEndTime());

        return schedule;
    }

    @Override
    public ProviderProfileResponse providerMapperToResponse(ProviderProfile providerProfile) {
        if (providerProfile == null) {
            return null;
        }
        return ProviderProfileResponse.builder()
                .user(providerProfile.getUser())
                .id(providerProfile.getId())
                .services(providerProfile.getServices())
                .city(providerProfile.getCity())
                .hourlyRate(providerProfile.getHourlyRate())
                .avgRating(providerProfile.getAvgRating())
                .status(providerProfile.getStatus())
                .build();
    }
}