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

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/create")
    public ResponseEntity<Job> createJob(@RequestBody JobRequestDTO dto) {
        return ResponseEntity.ok(jobService.createJob(dto));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<Job>> getProviderJobs(@PathVariable Long providerId) {
        return ResponseEntity.ok(jobService.getJobsForProvider(providerId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Job>> getCustomerJobs(@PathVariable Long customerId) {
        return ResponseEntity.ok(jobService.getJobsForCustomer(customerId));
    }

    // ✅ THIS IS THE ENDPOINT FOR "MARK RESOLVED"
    @PutMapping("/{id}/status")
    public ResponseEntity<Job> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(jobService.updateJobStatus(id, status));
    }
}