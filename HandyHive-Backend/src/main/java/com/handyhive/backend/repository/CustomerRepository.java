package com.handyhive.backend.repository;

import com.handyhive.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

    // ✅ FIX: Add Case-Insensitive Search here too
    Optional<Customer> findByEmailIgnoreCase(String email);
}