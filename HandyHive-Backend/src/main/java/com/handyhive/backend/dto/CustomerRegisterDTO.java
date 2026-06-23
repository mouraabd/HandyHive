package com.handyhive.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRegisterDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @JsonAlias({"passwordHash", "rawPassword"})
    private String password;
}