package com.handyhive.backend.service;

import com.handyhive.backend.dto.RatingRequestDTO;
import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.JobStatus;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Rating;
import com.handyhive.backend.repository.JobRepository;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final JobRepository jobRepository;
    private final ProviderRepository providerRepository;

    // Business Logic Constant: If rating drops below 2.0, trigger review
    private static final double QUALITY_REVIEW_THRESHOLD = 2.0;

    public RatingService(RatingRepository ratingRepository, JobRepository jobRepository, ProviderRepository providerRepository) {
        this.ratingRepository = ratingRepository;
        this.jobRepository = jobRepository;
        this.providerRepository = providerRepository;
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with id: " + id));
    }

    @Transactional
    public Rating createRating(RatingRequestDTO ratingRequest) {
        Job job = jobRepository.findById(ratingRequest.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + ratingRequest.getJobId()));

        if (job.getStatus() != JobStatus.COMPLETED) {
            throw new IllegalStateException("You can only rate completed jobs.");
        }

        if (job.getRating() != null) {
            throw new IllegalStateException("This job has already been rated.");
        }

        Rating rating = new Rating();
        rating.setScore(ratingRequest.getScore());
        rating.setComment(ratingRequest.getComment());
        rating.setJob(job);
        rating.setProvider(job.getProvider());
        // createdAt field removed if not in entity, otherwise: rating.setCreatedAt(OffsetDateTime.now());

        Rating savedRating = ratingRepository.save(rating);

        // Update the provider's average rating automatically
        updateProviderRating(job.getProvider());

        return savedRating;
    }

    /**
     * Recalculates and saves a provider's average rating.
     * Implements the "Quality Review Trigger" from the README.
     */
    private void updateProviderRating(Provider provider) {
        // Use the efficient query from RatingRepository
        // FIX: Changed .getId() to .getProviderId()
        Double newAvgRating = ratingRepository.calculateAverageRatingByProviderId(provider.getProviderId());

        if (newAvgRating == null) {
            newAvgRating = 0.0;
        }

        provider.setAvgRating(newAvgRating);

        // --- AUTOMATED QUALITY CONTROL (from README) ---
        if (newAvgRating < QUALITY_REVIEW_THRESHOLD && provider.getIsVetted()) {
            // Trigger the review process
            System.out.println("--- QUALITY ALERT ---");
            // FIX: Changed .getId() to .getProviderId()
            System.out.println("Provider ID " + provider.getProviderId() + " (" + provider.getFirstName() + ")");
            System.out.println("Rating dropped to " + newAvgRating + ". Triggering mandatory review.");

            // This is the technical action. You could also send an email, create a ticket, etc.
            provider.setIsVetted(false); // Revoke vetted status automatically
        }

        providerRepository.save(provider);
    }

    @Transactional
    public void deleteRating(Long id) {
        Rating rating = getRatingById(id);
        Provider provider = rating.getProvider();
        ratingRepository.delete(rating);

        // Recalculate after deletion
        updateProviderRating(provider);
    }
}