package com.handyhive.backend.service;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer registerCustomer(Customer customer) {
        // Check if email exists (Repository returns Customer or null)
        if (customerRepository.findByEmail(customer.getEmail()) != null) {
            throw new RuntimeException("Email already taken");
        }
        if (customerRepository.findByEmailIgnoreCase(customer.getEmail()).isPresent()) {
            throw new RuntimeException("Conflict: Email already taken");
        }

        // Encrypt password
        customer.setPasswordHash(passwordEncoder.encode(customer.getPasswordHash()));
        customer.setRole("CUSTOMER");
        return customerRepository.save(customer);
    }

    // ✅ FIX: Added missing method for Controller
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmailIgnoreCase(email);
    }

    // ✅ FIX: Added missing method for Controller
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
}