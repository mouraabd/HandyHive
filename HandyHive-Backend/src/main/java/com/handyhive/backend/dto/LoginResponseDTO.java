package com.handyhive.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;

    // Constructor required for the Controller to create this object
    public LoginResponseDTO(String token) {
        this.token = token;
    }
}