package com.handyhive.backend.service;

import com.handyhive.backend.dto.CustomerRegisterDTO;
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

    public CustomerService(
            CustomerRepository customerRepository,
            JobRepository jobRepository,
            PasswordEncoder passwordEncoder
    ) {
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
    public Customer registerCustomer(CustomerRegisterDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Customer registration request is required.");
        }

        String firstName = clean(request.getFirstName());
        String lastName = clean(request.getLastName());
        String email = clean(request.getEmail());
        String phoneNumber = clean(request.getPhoneNumber());
        String plainPassword = request.getPassword();

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        if (customerRepository.findByEmail(email) != null
                || customerRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new RuntimeException("Conflict: Email already taken");
        }

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(normalizeCzechPhone(phoneNumber));
        customer.setPasswordHash(passwordEncoder.encode(plainPassword));
        customer.setRole("CUSTOMER");
        customer.setRegistrationDate(OffsetDateTime.now());
        customer.setSubscriptionId(1);

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer patch) {
        Customer existing = getById(id);

        if (patch.getFirstName() != null) {
            existing.setFirstName(patch.getFirstName());
        }

        if (patch.getLastName() != null) {
            existing.setLastName(patch.getLastName());
        }

        if (patch.getPhoneNumber() != null) {
            existing.setPhoneNumber(normalizeCzechPhone(patch.getPhoneNumber()));
        }

        if (patch.getBio() != null) {
            existing.setBio(patch.getBio());
        }

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

        if (digits.startsWith("420")) {
            digits = digits.substring(3);
        }

        if (digits.length() != 9) {
            throw new IllegalArgumentException("Phone number must contain exactly 9 digits after +420. Example: +420 777 123 456");
        }

        return "+420 " + digits.substring(0, 3) + " " + digits.substring(3, 6) + " " + digits.substring(6, 9);
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }
}