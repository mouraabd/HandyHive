package com.handyhive.backend.service;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.repository.CustomerRepository;
import com.handyhive.backend.repository.ProviderRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final ProviderRepository providerRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       CustomerRepository customerRepository,
                       ProviderRepository providerRepository) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.providerRepository = providerRepository;
    }

    public Map<String, Object> login(String email, String password) {
        String normalizedEmail = email == null ? null : email.trim();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, password)
        );

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", normalizedEmail);
        payload.put("token", "dummy-token-12345");

        Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(normalizedEmail);
        if (customer.isPresent()) {
            Customer c = customer.get();
            payload.put("role", "CUSTOMER");
            payload.put("id", c.getId());
            payload.put("name", (c.getFirstName() == null ? "" : c.getFirstName()) + " " +
                    (c.getLastName() == null ? "" : c.getLastName()));
            return payload;
        }

        Optional<Provider> provider = providerRepository.findByEmailIgnoreCase(normalizedEmail);
        if (provider.isPresent()) {
            Provider p = provider.get();
            payload.put("role", "PROVIDER");
            payload.put("id", p.getProviderId());
            payload.put("providerId", p.getProviderId());
            payload.put("name", (p.getFirstName() == null ? "" : p.getFirstName()) + " " +
                    (p.getLastName() == null ? "" : p.getLastName()));
            return payload;
        }

        throw new IllegalArgumentException("Profile not found. Please register.");
    }
}
