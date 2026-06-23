package com.handyhive.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerUpdateDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String bio;
}
