package com.handyhive.backend.service;

import com.handyhive.backend.dto.RatingRequestDTO;
import com.handyhive.backend.dto.RatingUpdateDTO;
import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.JobStatus;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Rating;
import com.handyhive.backend.repository.JobRepository;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.RatingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final JobRepository jobRepository;
    private final ProviderRepository providerRepository;

    private static final double QUALITY_REVIEW_THRESHOLD = 2.0;

    public RatingService(RatingRepository ratingRepository,
                         JobRepository jobRepository,
                         ProviderRepository providerRepository) {
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

        Rating saved = ratingRepository.save(rating);
        updateProviderRating(job.getProvider());
        return saved;
    }

    @Transactional
    public Rating updateRating(Long ratingId, RatingUpdateDTO dto) {
        Rating rating = getRatingById(ratingId);

        if (dto.getScore() != null) rating.setScore(dto.getScore());
        if (dto.getComment() != null) rating.setComment(dto.getComment());

        Rating saved = ratingRepository.save(rating);
        updateProviderRating(saved.getProvider());
        return saved;
    }

    @Transactional
    public void deleteRating(Long id) {
        Rating rating = getRatingById(id);
        Provider provider = rating.getProvider();
        ratingRepository.delete(rating);
        updateProviderRating(provider);
    }

    private void updateProviderRating(Provider provider) {
        Double newAvg = ratingRepository.calculateAverageRatingByProviderId(provider.getProviderId());
        if (newAvg == null) newAvg = 0.0;

        provider.setAvgRating(newAvg);

        if (newAvg < QUALITY_REVIEW_THRESHOLD && Boolean.TRUE.equals(provider.getIsVetted())) {
            provider.setIsVetted(false);
        }

        providerRepository.save(provider);
    }
}
