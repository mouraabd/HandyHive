package com.handyhive.backend;

import com.handyhive.backend.model.Job;
import com.handyhive.backend.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JobRepositoryEdgeCaseTest {

    @Autowired
    private JobRepository jobRepository;

    @Test
    public void testFindJobsForNonExistentUser_ReturnsEmptyList() {
        // We do not save any jobs. The DB is empty.

        // 1. Search for a random User ID
        List<Job> jobs = jobRepository.findByCustomer_Id(9999L);

        // 2. Assert result is empty (not null)
        assertThat(jobs).isNotNull();
        assertThat(jobs).isEmpty();
    }
}