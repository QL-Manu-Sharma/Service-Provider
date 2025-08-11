package com.services.ServiceProvider.controller;

import com.services.ServiceProvider.dto.UserRequestDto;
import com.services.ServiceProvider.payload.request.PasswordRequest;
import com.services.ServiceProvider.payload.response.ApiResponse;
import com.services.ServiceProvider.service.EmailService;
import com.services.ServiceProvider.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createUser(@RequestBody @Valid UserRequestDto request , @RequestHeader("Authorization") String header) {
        String token = header.replace("Bearer ", "");
        ApiResponse apiResponse = userService.createUser(request,token);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getHttpStatusCode()));
    }

     @PostMapping(value = "/password/create", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<ApiResponse> createPassword(@RequestBody @Valid PasswordRequest request,
                                                 @RequestHeader("Authorization") String header) {
        String token = header.replace("Bearer ", "");
        ApiResponse apiResponse = userService.createPassword(request, token);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getHttpStatusCode()));
        }

    @PatchMapping(value = "/password/update", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody @Valid PasswordRequest request, Principal principal) {
        ApiResponse apiResponse = userService.updatePassword(request,principal.getName());
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getHttpStatusCode()));
    }
}