package com.handyhive.backend.service;

import com.handyhive.backend.model.AuthUser;
import com.handyhive.backend.model.Customer;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.repository.CustomerRepository;
import com.handyhive.backend.repository.ProviderRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final ProviderRepository providerRepository;

    public JpaUserDetailsService(CustomerRepository customerRepository, ProviderRepository providerRepository) {
        this.customerRepository = customerRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String cleanEmail = email.trim();

        // 1. Try Customer (Case Insensitive)
        Optional<Customer> customerOpt = customerRepository.findByEmailIgnoreCase(cleanEmail);
        if (customerOpt.isPresent()) {
            return new AuthUser(customerOpt.get());
        }

        // 2. Try Provider (Case Insensitive)
        Optional<Provider> providerOpt = providerRepository.findByEmailIgnoreCase(cleanEmail);
        if (providerOpt.isPresent()) {
            return new AuthUser(providerOpt.get());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}