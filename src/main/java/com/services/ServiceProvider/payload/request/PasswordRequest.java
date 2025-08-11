package com.services.ServiceProvider.payload.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordRequest {

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "Password must be alphanumeric (letters and numbers only)"
    )
    private String enteredPassword;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "Password must be alphanumeric (letters and numbers only)"
    )
    private String reenteredPassword;

}