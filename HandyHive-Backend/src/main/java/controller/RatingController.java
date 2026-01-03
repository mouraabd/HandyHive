package com.handyhive.backend.controller;

import com.handyhive.backend.dto.RatingRequestDTO;
import com.handyhive.backend.model.Rating;
import com.handyhive.backend.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/jobs/{jobId}/rating")
    public ResponseEntity<Rating> createRating(
            @PathVariable Long jobId,
            @jakarta.validation.Valid @RequestBody RatingRequestDTO ratingRequest) { // Ensure Valid is imported

        // FIX: Set the Job ID from the URL path into the DTO
        ratingRequest.setJobId(jobId);

        // FIX: Call the EXISTING method in your Service
        Rating newRating = ratingService.createRating(ratingRequest);

        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(newRating);
    }

}