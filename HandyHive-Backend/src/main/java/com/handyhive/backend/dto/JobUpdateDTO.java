package com.handyhive.backend.dto;

import lombok.Data;

@Data
public class JobUpdateDTO {
    private String description;
    private Boolean isUrgent;
    // Add any other fields you allow users to edit
}