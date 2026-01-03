package com.handyhive.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.List; // Added

@Getter
@Setter
public class ProviderRegisterDTO {

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    private String phoneNumber;
    private String bio;

    // New Field: List of Service IDs (e.g., [1, 2])
    private List<Long> serviceIds;
}