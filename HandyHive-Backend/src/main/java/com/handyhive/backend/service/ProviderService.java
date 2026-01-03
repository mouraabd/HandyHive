package com.handyhive.backend.service;

import com.handyhive.backend.model.Provider;
import com.handyhive.backend.repository.ProviderRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;

    // Folder to store uploads
    private final String UPLOAD_DIR = "uploads/";

    public ProviderService(ProviderRepository providerRepository, PasswordEncoder passwordEncoder) {
        this.providerRepository = providerRepository;
        this.passwordEncoder = passwordEncoder;

        // Create upload directory if it doesn't exist
        new File(UPLOAD_DIR).mkdirs();
    }

    // ✅ NEW: Register with Document
    public Provider registerProviderWithDoc(Provider provider, MultipartFile document) throws IOException {
        if (providerRepository.findByEmail(provider.getEmail()) != null) {
            throw new RuntimeException("Email already taken");
        }

        // 1. Handle File Upload
        if (document != null && !document.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + document.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(document.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            provider.setVettingDocPath(path.toString());
        }

        // 2. Standard Registration Logic
        provider.setPasswordHash(passwordEncoder.encode(provider.getPasswordHash()));
        provider.setRole("PROVIDER");

        if(provider.getSubscriptionId() == null) provider.setSubscriptionId(1);

        return providerRepository.save(provider);
    }

    // KEEP the old method for tests compatibility
    public Provider registerProvider(Provider provider) {
        try {
            return registerProviderWithDoc(provider, null);
        } catch (IOException e) {
            throw new RuntimeException("Error registering provider", e);
        }
    }

    public Provider getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
    }

    public Provider updateProviderDetails(Long id, Provider update) {
        Provider existing = getProviderById(id);
        if(update.getFirstName() != null) existing.setFirstName(update.getFirstName());
        if(update.getLastName() != null) existing.setLastName(update.getLastName());
        if(update.getPhoneNumber() != null) existing.setPhoneNumber(update.getPhoneNumber());
        if(update.getBio() != null) existing.setBio(update.getBio());
        return providerRepository.save(existing);
    }

    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        Provider provider = getProviderById(id);
        if (!passwordEncoder.matches(oldPassword, provider.getPasswordHash())) {
            throw new IllegalArgumentException("Incorrect current password");
        }
        provider.setPasswordHash(passwordEncoder.encode(newPassword));
        providerRepository.save(provider);
    }
}