package com.handyhive.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderUpdateDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String bio;
    private String currentStatus;
    private Boolean isVetted;
    private String password;
    private Integer subscriptionId; // Fixed: Added this
}