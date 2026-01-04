package com.handyhive.backend.service;

import com.handyhive.backend.dto.JobRequestDTO;
import com.handyhive.backend.model.Customer;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.JobStatus; // ✅ Import Enum
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.repository.CustomerRepository;
import com.handyhive.backend.repository.JobRepository;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.ServiceRepository;
import org.springframework.stereotype.Service; // ✅ Keep this for @Service annotation
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime; // ✅ Fix Date Error
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CustomerRepository customerRepository;
    private final ProviderRepository providerRepository;
    private final ServiceRepository serviceRepository;

    public JobService(JobRepository jobRepository,
                      CustomerRepository customerRepository,
                      ProviderRepository providerRepository,
                      ServiceRepository serviceRepository) {
        this.jobRepository = jobRepository;
        this.customerRepository = customerRepository;
        this.providerRepository = providerRepository;
        this.serviceRepository = serviceRepository;
    }

    public Job createJob(JobRequestDTO dto) {
        Customer customer = customerRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Provider provider = providerRepository.findById(dto.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        // ✅ Fix Ambiguity: Use fully qualified name for the Model
        com.handyhive.backend.model.Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Job job = new Job();
        job.setCustomer(customer);
        job.setProvider(provider);
        job.setServiceName(service.getName());
        job.setDescription(dto.getDescription());
        job.setIsUrgent(dto.getIsUrgent() != null ? dto.getIsUrgent() : false);

        // ✅ Fix: Use Enum instead of String
        job.setStatus(JobStatus.PENDING);

        // ✅ Fix: Use OffsetDateTime
        job.setDateCreated(OffsetDateTime.now());

        return jobRepository.save(job);
    }

    public List<Job> getJobsForProvider(Long providerId) {
        // ✅ This method must exist in JobRepository (See Step 2)
        return jobRepository.findByProvider_ProviderId(providerId);
    }

    public List<Job> getJobsForCustomer(Long customerId) {
        // ✅ This method must exist in JobRepository (See Step 2)
        return jobRepository.findByCustomer_Id(customerId);
    }

    @Transactional
    public Job updateJobStatus(Long jobId, String newStatusStr) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // ✅ Fix: Convert String to Enum safely
        JobStatus newStatus;
        try {
            newStatus = JobStatus.valueOf(newStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + newStatusStr);
        }

        // Logic: If status changes to COMPLETED, increment count
        if (JobStatus.COMPLETED.equals(newStatus) && !JobStatus.COMPLETED.equals(job.getStatus())) {
            Provider provider = job.getProvider();
            if (provider != null) {
                int currentCount = provider.getJobsCompleted() == null ? 0 : provider.getJobsCompleted();
                provider.setJobsCompleted(currentCount + 1);
                providerRepository.save(provider);
            }
        }

        job.setStatus(newStatus);
        return jobRepository.save(job);
    }


    // ✅ NEW: Get Job by ID (used by the Controller)
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new com.handyhive.backend.exception.ResourceNotFoundException("Job not found with id: " + id));
    }
}