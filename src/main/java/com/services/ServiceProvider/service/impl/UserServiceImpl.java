package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.constant.Constants;
import com.services.ServiceProvider.dto.UserRequestDto;
import com.services.ServiceProvider.dto.UserResponseDto;
import com.services.ServiceProvider.entity.Role;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.exception.RolesNotAssignException;
import com.services.ServiceProvider.exception.UserNotFoundException;
import com.services.ServiceProvider.payload.request.PasswordRequest;
import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.repository.RoleRepository;
import com.services.ServiceProvider.repository.UserRepository;
import com.services.ServiceProvider.security.JwtHelper;
import com.services.ServiceProvider.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtHelper helper;
    private final PasswordEncoder encoder;
    private final OtpServiceImpl otpService;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;

    public UserServiceImpl(UserRepository userRepository, JwtHelper helper, PasswordEncoder encoder, OtpServiceImpl otpService, RoleRepository roleRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.helper = helper;
        this.encoder = encoder;
        this.otpService = otpService;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public ApiResponse createUser(UserRequestDto requestDto, String token) {
        String username = helper.getUserNameFromToken(token);
        User user = getUserByUserName(username);

        if (Boolean.TRUE.equals(user.getIsDummyUser())) {
            user.setName(requestDto.getName());
            user.setContactNumber(requestDto.getContactNumber());
            user.setAddress(requestDto.getAddress());
            user.setIsBlocked(false);
            user.setIsEmailVerified(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(null);
            updateUser(username,user);
        }
        UserResponseDto userResponseDto = mapUserToUserDto(requestDto);
        return ApiResponse.builder().data(userResponseDto).status(true).code(118).build();
    }

    @Override
    public ApiResponse createPassword(PasswordRequest request, String token) {
        String username = helper.getUserNameFromToken(token);
        User user = getUserByUserName(username);
        ApiResponse apiResponse =new ApiResponse();
        if (user != null && !Boolean.TRUE.equals(user.getIsPasswordCreated())) {
            if (request.getEnteredPassword().equals(request.getReenteredPassword())) {
                user.setPassword(encoder.encode(request.getEnteredPassword()));
                user.setIsPasswordCreated(true);
                updateUser(username,user);
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("isPasswordCreated", true);
                apiResponse = ApiResponse.builder().code(125).status(true).data(responseData).build();
                return apiResponse;
            }
            apiResponse =ApiResponse.builder().code(431).status(false).build();
            return apiResponse;
        }
        apiResponse =ApiResponse.builder().code(114).status(false).build();
        return apiResponse;
    }

    @Override
    public User getUserByUserName(String username) {
        // 1. Try to get the user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                User loggedInUser = (User) principal;
                if (loggedInUser.getUsername().equals(username)) {
                    return loggedInUser; // ✅ Avoid DB hit
                }
            }

            // If using Spring Security's UserDetails
            if (principal instanceof UserDetails) {
                String currentUsername = ((UserDetails) principal).getUsername();
                if (currentUsername.equals(username)) {
                    // If needed, you could return a wrapped User or fetch from cache here
                    // This block depends on how your principal is stored
                }
            }
        }

        // 2. Fallback to DB if user is not in context
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(410, username, Constants.RESPONSE.get(410));
        }

        return user;
    }

    @Override
    public void deleteUser(String username) {
        User user = getUserByUserName(username);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void updateUser(String userName , User updatedUser) {
        User existingUser = getUserByUserName(userName);
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getAddress() != null) {
            existingUser.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getContactNumber() != null) {
            existingUser.setContactNumber(updatedUser.getContactNumber());
        }

        if (updatedUser.getIsBlocked() != null) {
            existingUser.setIsBlocked(updatedUser.getIsBlocked());
        }
        if (updatedUser.getIsEmailVerified() != null) {
            existingUser.setIsEmailVerified(updatedUser.getIsEmailVerified());
        }
        if (updatedUser.getIsPasswordCreated() != null) {
            existingUser.setIsPasswordCreated(updatedUser.getIsPasswordCreated());
        }

        if (updatedUser.getIsDummyUser() != null) {
            existingUser.setIsDummyUser(updatedUser.getIsDummyUser());
        }
        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            existingUser.setRoles(updatedUser.getRoles());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(existingUser);
    }

    @Override
    public ApiResponse updatePassword(PasswordRequest request, String userName) {
        User existingUser = getUserByUserName(userName);
        if(existingUser == null){
            throw new UserNotFoundException(410,userName,Constants.RESPONSE.get(410));
        }
        if(request.getEnteredPassword().equals(request.getReenteredPassword())){
            existingUser.setPassword(encoder.encode(request.getEnteredPassword()));
            updateUser(userName,existingUser);
            return ApiResponse.builder().status(true).code(129).build();
        }
        return ApiResponse.builder().status(false).code(431).build();
    }


    @Override
    public UserResponseDto mapUserToUserDto(UserRequestDto dto) {
        UserResponseDto response = new UserResponseDto();
        response.setName(dto.getName());
        response.setContactNumber(dto.getContactNumber());
        response.setAddress(dto.getAddress());
        return response;
    }

    @Override
    @Transactional
    public Set<Role> assignRoleToUser(String email, String roleName) {
        try {
            // Fetch user by email
            User user = getUserByUserName(email);
            if (user == null) {
                throw new IllegalArgumentException("User not found with email: " + email);
            }

            // Fetch role by role name
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

            // Initialize roles if null
            Set<Role> roles = Optional.ofNullable(user.getRoles()).orElseGet(() -> {
                Set<Role> newRoles = new HashSet<>();
                user.setRoles(newRoles);
                return newRoles;
            });

            // Add role if not already present
            if (roles.add(role)) {
                userRepository.save(user);
            }

            return roles;

        } catch (Exception ex) {
            // Handle unexpected exceptions
            throw  new RolesNotAssignException(448,Constants.RESPONSE.get(448));
        }
    }

    @Override
    public UserResponseDto userMapperToResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setName(user.getName());
        dto.setContactNumber(user.getContactNumber());
        dto.setAddress(user.getAddress());

        return dto;
    }

}