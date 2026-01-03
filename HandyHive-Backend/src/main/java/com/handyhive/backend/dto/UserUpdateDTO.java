package com.handyhive.backend.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private Integer subscriptionId; // Added
}