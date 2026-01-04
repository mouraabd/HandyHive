package com.handyhive.backend;

import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Rating;
import com.handyhive.backend.repository.JobRepository;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RatingRepositoryTest {

    @Autowired private RatingRepository ratingRepository;
    @Autowired private ProviderRepository providerRepository;
    @Autowired private JobRepository jobRepository; // ✅ Need this

    @Test
    public void testCalculateAverageRating() {
        // 1. Create Provider
        Provider provider = new Provider();
        provider.setFirstName("Rated Provider");
        provider.setLastName("Test"); // ✅ Required
        provider.setPasswordHash("123"); // ✅ Required
        provider.setEmail("rated@test.com");
        providerRepository.save(provider);

        // 2. Create Dummy Jobs (Ratings must be linked to a job)
        Job job1 = new Job();
        job1.setProvider(provider);
        jobRepository.save(job1); // ✅ Save Job

        Job job2 = new Job();
        job2.setProvider(provider);
        jobRepository.save(job2); // ✅ Save Job

        // 3. Add Ratings linked to jobs
        Rating r1 = new Rating();
        r1.setProvider(provider);
        r1.setJob(job1); // ✅ Link to Job
        r1.setScore(5.0);
        ratingRepository.save(r1);

        Rating r2 = new Rating();
        r2.setProvider(provider);
        r2.setJob(job2); // ✅ Link to Job
        r2.setScore(3.0);
        ratingRepository.save(r2);

        // 4. Run Query
        Double avg = ratingRepository.calculateAverageRatingByProviderId(provider.getProviderId());
        assertThat(avg).isEqualTo(4.0);
    }
}