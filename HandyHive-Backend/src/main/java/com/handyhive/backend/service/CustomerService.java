package com.handyhive.backend.service;

import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.Customer;
import com.handyhive.backend.repository.CustomerRepository;
import com.handyhive.backend.repository.JobRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, JobRepository jobRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.jobRepository = jobRepository;
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

    @Transactional
    public Customer registerCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()) != null
                || customerRepository.findByEmailIgnoreCase(customer.getEmail()).isPresent()) {
            throw new RuntimeException("Conflict: Email already taken");
        }

        customer.setPhoneNumber(normalizeCzechPhone(customer.getPhoneNumber()));
        customer.setPasswordHash(passwordEncoder.encode(customer.getPasswordHash()));
        customer.setRole("CUSTOMER");
        if (customer.getRegistrationDate() == null) customer.setRegistrationDate(OffsetDateTime.now());
        if (customer.getSubscriptionId() == null) customer.setSubscriptionId(1);

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer patch) {
        Customer existing = getById(id);

        if (patch.getFirstName() != null) existing.setFirstName(patch.getFirstName());
        if (patch.getLastName() != null) existing.setLastName(patch.getLastName());
        if (patch.getPhoneNumber() != null) existing.setPhoneNumber(normalizeCzechPhone(patch.getPhoneNumber()));
        if (patch.getBio() != null) existing.setBio(patch.getBio());

        return customerRepository.save(existing);
    }

    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        Customer customer = getById(id);
        if (oldPassword == null || newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }
        if (!passwordEncoder.matches(oldPassword, customer.getPasswordHash())) {
            throw new IllegalArgumentException("Incorrect current password");
        }
        customer.setPasswordHash(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getById(id);
        jobRepository.deleteAll(jobRepository.findByCustomer_Id(id));
        customerRepository.delete(customer);
    }

    private String normalizeCzechPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number is required.");
        }
        String digits = phone.replaceAll("\\D", "");
        if (digits.startsWith("420")) digits = digits.substring(3);
        if (digits.length() != 9) {
            throw new IllegalArgumentException("Phone number must contain exactly 9 digits after +420. Example: +420 777 123 456");
        }
        return "+420 " + digits.substring(0, 3) + " " + digits.substring(3, 6) + " " + digits.substring(6, 9);
    }
}
