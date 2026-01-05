package com.handyhive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponseDTO {

    private Long providerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String bio;

    private Boolean isVetted;
    private Integer subscriptionId;
    private String role;
    private Double avgRating;
    private Integer jobsCompleted;

    private String vettingDocPath;

    // Optional: show assigned services as IDs (safe)
    private List<Long> serviceIds;
}
