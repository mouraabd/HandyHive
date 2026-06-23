package com.handyhive.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobRequestDTO {

    @NotNull(message = "customer/user id is required")
    private Long userId;

    @NotNull(message = "providerId is required")
    private Long providerId;

    @NotNull(message = "serviceId is required")
    private Long serviceId;

    private String description;
    private Boolean isUrgent;
}