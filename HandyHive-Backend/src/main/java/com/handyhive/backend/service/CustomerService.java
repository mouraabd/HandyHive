package com.handyhive.backend.service;

import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.Customer;
import com.handyhive.backend.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmailIgnoreCase(email == null ? null : email.trim());
    }

    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public Customer registerCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()) != null
                || customerRepository.findByEmailIgnoreCase(customer.getEmail()).isPresent()) {
            throw new RuntimeException("Conflict: Email already taken");
        }

        customer.setPasswordHash(passwordEncoder.encode(customer.getPasswordHash()));
        customer.setRole("CUSTOMER");
        if (customer.getRegistrationDate() == null) customer.setRegistrationDate(OffsetDateTime.now());
        if (customer.getSubscriptionId() == null) customer.setSubscriptionId(1);

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer patch) {
        Customer existing = getById(id);

        if (patch.getFirstName() != null) existing.setFirstName(patch.getFirstName());
        if (patch.getLastName() != null) existing.setLastName(patch.getLastName());
        if (patch.getPhoneNumber() != null) existing.setPhoneNumber(patch.getPhoneNumber());

        return customerRepository.save(existing);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}
