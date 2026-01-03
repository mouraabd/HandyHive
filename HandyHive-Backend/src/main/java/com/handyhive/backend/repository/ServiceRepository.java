package com.handyhive.backend.repository;

import com.handyhive.backend.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    // This allows us to find "Plumbing" by its name
    Optional<Service> findByName(String name);
}