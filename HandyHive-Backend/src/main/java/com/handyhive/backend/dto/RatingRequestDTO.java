package com.handyhive.backend.dto;

import lombok.Data;

@Data
public class RatingRequestDTO {
    private Long jobId;
    private Double score;
    private String comment;
}