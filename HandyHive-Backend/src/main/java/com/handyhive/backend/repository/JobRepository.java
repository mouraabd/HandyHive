package com.handyhive.backend.repository;

import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByProvider_ProviderId(Long providerId);
    List<Job> findByCustomer_Id(Long customerId);

    // ✅ used by smart matching to balance workload (PENDING + IN_PROGRESS)
    long countByProvider_ProviderIdAndStatusIn(Long providerId, Collection<JobStatus> statuses);

    void deleteByCustomer_Id(Long customerId);
    void deleteByProvider_ProviderId(Long providerId);
}
