package com.handyhive.backend;

import com.handyhive.backend.dto.RatingRequestDTO;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.JobStatus;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void createRating_completedJob_savesAndUpdatesProviderAvg() {
        Long jobId = 1L;

        Provider provider = new Provider();
        provider.setProviderId(2L);
        provider.setIsVetted(true);

        Job job = new Job();
        job.setJobId(jobId);
        job.setStatus(JobStatus.COMPLETED);
        job.setProvider(provider);
        job.setRating(null);

        RatingRequestDTO dto = new RatingRequestDTO();
        dto.setJobId(jobId);
        dto.setScore(5.0);
        dto.setComment("Great!");

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        // save returns the entity (with ID set)
        when(ratingRepository.save(any(Rating.class))).thenAnswer(inv -> {
            Rating r = inv.getArgument(0);
            r.setRatingId(10L);
            return r;
        });

        when(ratingRepository.calculateAverageRatingByProviderId(2L)).thenReturn(4.2);
        when(providerRepository.save(any(Provider.class))).thenAnswer(inv -> inv.getArgument(0));

        Rating result = ratingService.createRating(dto);

        assertNotNull(result);
        assertEquals(10L, result.getRatingId());
        assertEquals(5.0, result.getScore());
        assertEquals("Great!", result.getComment());
        assertEquals(job, result.getJob());
        assertEquals(provider, result.getProvider());

        // provider avg updated
        assertEquals(4.2, provider.getAvgRating());

        verify(ratingRepository).save(any(Rating.class));
        verify(providerRepository, atLeastOnce()).save(any(Provider.class));
    }

    @Test
    void createRating_notCompleted_throws() {
        Long jobId = 1L;

        Provider provider = new Provider();
        provider.setProviderId(2L);

        Job job = new Job();
        job.setJobId(jobId);
        job.setStatus(JobStatus.PENDING);
        job.setProvider(provider);

        RatingRequestDTO dto = new RatingRequestDTO();
        dto.setJobId(jobId);
        dto.setScore(3.0);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        assertThrows(IllegalStateException.class, () -> ratingService.createRating(dto));
        verify(ratingRepository, never()).save(any());
    }
}
