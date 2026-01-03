package com.handyhive.backend.repository;

import com.handyhive.backend.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Provider findByEmail(String email);

    // FIX: Add Case-Insensitive Search
    Optional<Provider> findByEmailIgnoreCase(String email);
    List<Provider> findByServices_ServiceId(Long serviceId);

    // Match service for Checkout
    @Query("SELECT p FROM Provider p JOIN p.services s WHERE s.id = :serviceId")
    List<Provider> findByServiceId(@Param("serviceId") Long serviceId);
}