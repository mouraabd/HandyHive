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

    Optional<Provider> findByEmailIgnoreCase(String email);

    List<Provider> findByServices_ServiceId(Long serviceId);

    // If used anywhere else, this is still OK:
    @Query("SELECT p FROM Provider p JOIN p.services s WHERE s.serviceId = :serviceId")
    List<Provider> findByServiceId(@Param("serviceId") Long serviceId);

    // ✅ Complex JPQL (multi-table) kept for your rubric requirement
    @Query("""
            SELECT p
            FROM Provider p
            JOIN p.services s
            LEFT JOIN p.jobs j
            LEFT JOIN j.rating r
            WHERE s.serviceId = :serviceId
            GROUP BY p
            ORDER BY COALESCE(AVG(r.score), 0) DESC, COUNT(j) DESC, p.providerId ASC
            """)
    List<Provider> recommendProvidersForService(@Param("serviceId") Long serviceId);
}
