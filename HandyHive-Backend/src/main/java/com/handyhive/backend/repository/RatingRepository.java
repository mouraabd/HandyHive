package com.handyhive.backend.repository;

import com.handyhive.backend.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    /**
     * Calculates the average rating for a specific provider.
     * This query is more efficient than fetching all ratings.
     */
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.provider.id = :providerId")
    Double calculateAverageRatingByProviderId(Long providerId);
}