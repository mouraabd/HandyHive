package com.handyhive.backend.controller;

import com.handyhive.backend.dto.RatingRequestDTO;
import com.handyhive.backend.dto.RatingUpdateDTO;
import com.handyhive.backend.model.Rating;
import com.handyhive.backend.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // ✅ CRUD READ (list)
    @GetMapping("/ratings")
    public ResponseEntity<List<Rating>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatings());
    }

    // ✅ CRUD READ (by id)
    @GetMapping("/ratings/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getRatingById(id));
    }

    // ✅ CRUD CREATE (generic)
    @PostMapping("/ratings")
    public ResponseEntity<Rating> createRating(@Valid @RequestBody RatingRequestDTO dto) {
        Rating saved = ratingService.createRating(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/ratings/{id}")
                .buildAndExpand(saved.getRatingId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // ✅ REQUIRED endpoint you already had: POST /api/jobs/{jobId}/rating
    @PostMapping("/jobs/{jobId}/rating")
    public ResponseEntity<Rating> createRatingForJob(
            @PathVariable Long jobId,
            @Valid @RequestBody RatingRequestDTO dto) {

        dto.setJobId(jobId);
        Rating saved = ratingService.createRating(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/ratings/{id}")
                .buildAndExpand(saved.getRatingId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // ✅ CRUD UPDATE
    @PutMapping("/ratings/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable Long id, @RequestBody RatingUpdateDTO dto) {
        return ResponseEntity.ok(ratingService.updateRating(id, dto));
    }

    // ✅ CRUD DELETE
    @DeleteMapping("/ratings/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}
