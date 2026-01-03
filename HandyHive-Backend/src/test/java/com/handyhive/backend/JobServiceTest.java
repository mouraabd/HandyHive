package com.handyhive.backend;

import com.handyhive.backend.dto.JobRequestDTO;
import com.handyhive.backend.model.*;
import com.handyhive.backend.repository.CustomerRepository;
import com.handyhive.backend.repository.JobRepository;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.ServiceRepository;
import com.handyhive.backend.service.JobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    public void testCreateJob() {
        // Arrange
        JobRequestDTO dto = new JobRequestDTO();
        dto.setUserId(1L);
        dto.setProviderId(2L);
        dto.setServiceId(3L);
        dto.setDescription("Fix leak");
        dto.setIsUrgent(true);

        Customer customer = new Customer();
        customer.setId(1L);

        Provider provider = new Provider();
        provider.setProviderId(2L);

        // ✅ Fix Ambiguity: Fully qualified name
        com.handyhive.backend.model.Service service = new com.handyhive.backend.model.Service();
        service.setServiceId(3L);
        service.setName("Plumbing");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(providerRepository.findById(2L)).thenReturn(Optional.of(provider));
        when(serviceRepository.findById(3L)).thenReturn(Optional.of(service));

        Job savedJob = new Job();
        savedJob.setJobId(100L);
        // ✅ Fix: Use Enum
        savedJob.setStatus(JobStatus.PENDING);
        // ✅ Fix: Use OffsetDateTime
        savedJob.setDateCreated(OffsetDateTime.now());

        when(jobRepository.save(any(Job.class))).thenReturn(savedJob);

        // Act
        Job result = jobService.createJob(dto);

        // Assert
        assertEquals(JobStatus.PENDING, result.getStatus());
        assertEquals(100L, result.getJobId());
    }
}