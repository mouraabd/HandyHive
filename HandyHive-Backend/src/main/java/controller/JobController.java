package com.handyhive.backend.controller;

import com.handyhive.backend.dto.JobRequestDTO;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    // Constructor Injection
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // ✅ 1. Create Job (REST Standard: POST /api/jobs)
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobRequestDTO dto) {
        return ResponseEntity.ok(jobService.createJob(dto));
    }

    // ✅ 2. Get Job By ID (Fixes the 404 Test)
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // ✅ 3. Get Jobs for Provider
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<Job>> getProviderJobs(@PathVariable Long providerId) {
        return ResponseEntity.ok(jobService.getJobsForProvider(providerId));
    }

    // ✅ 4. Get Jobs for Customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Job>> getCustomerJobs(@PathVariable Long customerId) {
        return ResponseEntity.ok(jobService.getJobsForCustomer(customerId));
    }

    // ✅ 5. Update Status
    @PutMapping("/{id}/status")
    public ResponseEntity<Job> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(jobService.updateJobStatus(id, status));
    }
}