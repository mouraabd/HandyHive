package com.handyhive.backend.repository;

import com.handyhive.backend.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // ✅ Declare these so Spring generates the query for you
    List<Job> findByProvider_ProviderId(Long providerId);
    List<Job> findByCustomer_Id(Long customerId);
}