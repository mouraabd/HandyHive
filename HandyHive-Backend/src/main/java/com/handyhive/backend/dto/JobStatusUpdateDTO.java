package com.handyhive.backend.dto;

import com.handyhive.backend.model.JobStatus; // Import the standalone enum
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobStatusUpdateDTO {

    @NotNull(message = "Status is required")
    private JobStatus status; // Use JobStatus directly, NOT Job.JobStatus
}