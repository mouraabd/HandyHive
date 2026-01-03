package com.handyhive.backend.dto;

import lombok.Data;

@Data
public class JobRequestDTO {
    private Long userId;
    private Long providerId;
    private Long serviceId;
    private String description;
    private Boolean isUrgent;
}