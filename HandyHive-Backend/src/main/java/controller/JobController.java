package com.handyhive.backend.controller;

import com.handyhive.backend.dto.JobRequestDTO;
import jakarta.validation.Valid;
import com.handyhive.backend.dto.JobUpdateDTO;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // ✅ READ (list)
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // ✅ CREATE (201 Created + Location)
    @PostMapping
    public ResponseEntity<Job> createJob(@Valid @RequestBody JobRequestDTO dto) {
        Job saved = jobService.createJob(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getJobId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // ✅ READ (by id)
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // ✅ READ (provider jobs)
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<Job>> getProviderJobs(@PathVariable Long providerId) {
        return ResponseEntity.ok(jobService.getJobsForProvider(providerId));
    }

    // ✅ READ (customer jobs)
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Job>> getCustomerJobs(@PathVariable Long customerId) {
        return ResponseEntity.ok(jobService.getJobsForCustomer(customerId));
    }

    // ✅ UPDATE (details)
    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody JobUpdateDTO dto) {
        return ResponseEntity.ok(jobService.updateJobDetails(id, dto));
    }

    // ✅ UPDATE (status)
    @PutMapping("/{id}/status")
    public ResponseEntity<Job> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(jobService.updateJobStatus(id, status));
    }

    // ✅ DELETE (204)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
