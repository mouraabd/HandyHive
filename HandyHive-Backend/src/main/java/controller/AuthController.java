package com.handyhive.backend.controller;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.repository.CustomerRepository;
import com.handyhive.backend.repository.ProviderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final ProviderRepository providerRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          CustomerRepository customerRepository,
                          ProviderRepository providerRepository) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.providerRepository = providerRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        System.out.println("----- LOGIN ATTEMPT -----");
        System.out.println("Email provided: " + email);

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing email or password"));
        }

        // 1. Normalize Email
        String cleanEmail = email.trim();

        try {
            // 2. Authenticate (Check Password)
            System.out.println("Authenticating user...");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(cleanEmail, password)
            );
            System.out.println("Authentication Successful!");

            Map<String, Object> response = new HashMap<>();
            response.put("email", cleanEmail);
            response.put("token", "dummy-token-12345");

            // 3. Find User Profile (Using IgnoreCase)
            System.out.println("Searching for profile (Customer)...");
            Optional<Customer> cust = customerRepository.findByEmailIgnoreCase(cleanEmail);
            if (cust.isPresent()) {
                System.out.println("Found CUSTOMER: " + cust.get().getFirstName());
                response.put("role", "CUSTOMER");
                response.put("id", cust.get().getId());
                response.put("name", cust.get().getFirstName() + " " + cust.get().getLastName());
                return ResponseEntity.ok(response);
            }

            System.out.println("Searching for profile (Provider)...");
            Optional<Provider> prov = providerRepository.findByEmailIgnoreCase(cleanEmail);
            if (prov.isPresent()) {
                System.out.println("Found PROVIDER: " + prov.get().getFirstName());
                response.put("role", "PROVIDER");
                response.put("providerId", prov.get().getProviderId());
                response.put("id", prov.get().getProviderId());
                response.put("name", prov.get().getFirstName() + " " + prov.get().getLastName());
                return ResponseEntity.ok(response);
            }

            System.out.println("❌ ERROR: Profile not found in database.");
            return ResponseEntity.status(401).body(Map.of("error", "Profile not found. Please register."));

        } catch (Exception e) {
            System.out.println("❌ ERROR: Authentication Failed. " + e.getMessage());
            // ✅ CRITICAL FIX: Return JSON, not string, to prevent "Unexpected token I"
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}