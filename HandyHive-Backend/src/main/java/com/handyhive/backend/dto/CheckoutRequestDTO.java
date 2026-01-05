package com.handyhive.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequestDTO {
    private Long customerId;
    private List<Long> serviceIds;
    private String description;
    private Boolean urgent;
}
