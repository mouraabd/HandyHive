package com.handyhive.backend;

import com.handyhive.backend.dto.JobRequestDTO;
import com.handyhive.backend.model.Customer;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.JobStatus;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.repository.CustomerRepository;
import com.handyhive.backend.repository.JobRepository;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.RatingRepository;
import com.handyhive.backend.repository.ServiceRepository;
import com.handyhive.backend.service.JobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    void createJob_setsPendingAndServiceName() {
        JobRequestDTO dto = new JobRequestDTO();
        dto.setUserId(1L);
        dto.setProviderId(2L);
        dto.setServiceId(3L);
        dto.setDescription("Fix leaking pipe");
        dto.setIsUrgent(true);

        Customer customer = new Customer();
        customer.setId(1L);

        Provider provider = new Provider();
        provider.setProviderId(2L);

        com.handyhive.backend.model.Service service = new com.handyhive.backend.model.Service();
        service.setServiceId(3L);
        service.setName("Plumbing");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(providerRepository.findById(2L)).thenReturn(Optional.of(provider));
        when(serviceRepository.findById(3L)).thenReturn(Optional.of(service));

        when(jobRepository.save(any(Job.class))).thenAnswer(inv -> {
            Job j = inv.getArgument(0);
            j.setJobId(10L);
            return j;
        });

        Job created = jobService.createJob(dto);

        assertNotNull(created);
        assertEquals(10L, created.getJobId());
        assertEquals(JobStatus.PENDING, created.getStatus());
        assertEquals("Plumbing", created.getServiceName());
        assertTrue(created.getIsUrgent());
        assertEquals("Fix leaking pipe", created.getDescription());
        assertNotNull(created.getDateCreated());
    }
}
