package com.services.ServiceProvider.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 10)
    private String contactNumber;

    @Size(max = 100)
    private String address;



}