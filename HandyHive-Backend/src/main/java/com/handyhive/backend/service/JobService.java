package com.handyhive.backend.service;

import com.handyhive.backend.dto.JobRequestDTO;
import com.handyhive.backend.dto.JobUpdateDTO;
import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.*;
import com.handyhive.backend.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@org.springframework.stereotype.Service
public class JobService {

    private final JobRepository jobRepository;
    private final CustomerRepository customerRepository;
    private final ProviderRepository providerRepository;
    private final ServiceRepository serviceRepository;
    private final RatingRepository ratingRepository;

    public JobService(JobRepository jobRepository,
                      CustomerRepository customerRepository,
                      ProviderRepository providerRepository,
                      ServiceRepository serviceRepository,
                      RatingRepository ratingRepository) {
        this.jobRepository = jobRepository;
        this.customerRepository = customerRepository;
        this.providerRepository = providerRepository;
        this.serviceRepository = serviceRepository;
        this.ratingRepository = ratingRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
    }

    public Job createJob(JobRequestDTO dto) {
        Customer customer = customerRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getUserId()));

        Provider provider = providerRepository.findById(dto.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + dto.getProviderId()));

        // Avoid Service name conflict
        com.handyhive.backend.model.Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + dto.getServiceId()));

        Job job = new Job();
        job.setCustomer(customer);
        job.setProvider(provider);
        job.setServiceName(service.getName());
        job.setDescription(dto.getDescription());
        job.setIsUrgent(dto.getIsUrgent() != null ? dto.getIsUrgent() : false);
        job.setStatus(JobStatus.PENDING);
        job.setDateCreated(OffsetDateTime.now());

        return jobRepository.save(job);
    }

    public List<Job> getJobsForProvider(Long providerId) {
        return jobRepository.findByProvider_ProviderId(providerId);
    }

    public List<Job> getJobsForCustomer(Long customerId) {
        return jobRepository.findByCustomer_Id(customerId);
    }

    @Transactional
    public Job updateJobDetails(Long jobId, JobUpdateDTO dto) {
        Job job = getJobById(jobId);

        if (dto.getDescription() != null) job.setDescription(dto.getDescription());
        if (dto.getIsUrgent() != null) job.setIsUrgent(dto.getIsUrgent());

        return jobRepository.save(job);
    }

    @Transactional
    public Job updateJobStatus(Long jobId, String newStatusStr) {
        Job job = getJobById(jobId);

        JobStatus newStatus;
        try {
            newStatus = JobStatus.valueOf(newStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + newStatusStr);
        }

        // If status changes to COMPLETED, increment count
        if (JobStatus.COMPLETED.equals(newStatus) && !JobStatus.COMPLETED.equals(job.getStatus())) {
            Provider provider = job.getProvider();
            if (provider != null) {
                int current = provider.getJobsCompleted() == null ? 0 : provider.getJobsCompleted();
                provider.setJobsCompleted(current + 1);
                providerRepository.save(provider);
            }
        }

        job.setStatus(newStatus);
        return jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long jobId) {
        Job job = getJobById(jobId);

        // If job has rating, delete it first to avoid FK constraint issues
        if (job.getRating() != null) {
            ratingRepository.delete(job.getRating());
        }

        // If deleting a completed job, decrement provider count (keeps data consistent)
        if (JobStatus.COMPLETED.equals(job.getStatus()) && job.getProvider() != null) {
            Provider provider = job.getProvider();
            int current = provider.getJobsCompleted() == null ? 0 : provider.getJobsCompleted();
            if (current > 0) provider.setJobsCompleted(current - 1);
            providerRepository.save(provider);
        }

        jobRepository.delete(job);
    }
}
