package com.services.ServiceProvider.service;

import com.services.ServiceProvider.dto.UserRequestDto;
import com.services.ServiceProvider.dto.UserResponseDto;
import com.services.ServiceProvider.entity.Role;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.payload.request.PasswordRequest;
import com.services.ServiceProvider.payload.response.ApiResponse;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public interface UserService {
     ApiResponse createPassword(PasswordRequest request, String token);
     ApiResponse createUser(UserRequestDto requestDto, String token);

     UserResponseDto mapUserToUserDto(UserRequestDto dto);

     Set<Role> assignRoleToUser(String email, String roleName);

     User getUserByUserName(String username);

     void deleteUser(String username);

     void updateUser(String userName , User updatedUser);

     ApiResponse updatePassword(PasswordRequest request,String userName);

     UserResponseDto userMapperToResponseDto(User user);
}