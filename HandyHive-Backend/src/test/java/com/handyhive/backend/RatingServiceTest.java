package com.handyhive.backend;

import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Rating;
import com.handyhive.backend.repository.JobRepository;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.RatingRepository;
import com.handyhive.backend.service.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock private RatingRepository ratingRepository;
    @Mock private JobRepository jobRepository;
    @Mock private ProviderRepository providerRepository;

    @InjectMocks private RatingService ratingService;

    @Test
    public void testAddRating_Success() {
        // 1. Arrange
        Long jobId = 100L;
        Job mockJob = new Job();
        mockJob.setJobId(jobId);

        Provider mockProvider = new Provider();
        mockProvider.setProviderId(50L);
        mockJob.setProvider(mockProvider);

        Rating inputRating = new Rating();
        inputRating.setScore(5.0); // ✅ FIXED: Use 5.0 (Double)
        inputRating.setComment("Great job!");

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(mockJob));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(i -> i.getArguments()[0]);

        // 2. Act
        Rating result = ratingService.addRating(jobId, inputRating);

        // 3. Assert
        assertNotNull(result);
        assertEquals(5.0, result.getScore()); // ✅ FIXED: Use 5.0
        assertEquals(mockJob, result.getJob());
        assertEquals(mockProvider, result.getProvider());

        verify(ratingRepository).save(any(Rating.class));
    }
}