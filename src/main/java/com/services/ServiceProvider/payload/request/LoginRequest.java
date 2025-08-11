package com.services.ServiceProvider.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginRequest {

    @Email
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "Password must be alphanumeric (letters and numbers only)"
    )
    private String password;



}