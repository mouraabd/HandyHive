package com.handyhive.backend.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AuthUser implements UserDetails {

    private final String email;
    private final String password;
    private final String role;
    private final Long id; // Store ID for reference if needed

    // Constructor for Customer
    public AuthUser(Customer customer) {
        this.email = customer.getEmail();
        this.password = customer.getPasswordHash();
        this.role = "ROLE_CUSTOMER";
        this.id = customer.getId();
    }

    // Constructor for Provider
    public AuthUser(Provider provider) {
        this.email = provider.getEmail();
        this.password = provider.getPasswordHash();
        this.role = "ROLE_PROVIDER";
        this.id = provider.getProviderId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}