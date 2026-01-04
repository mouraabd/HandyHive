package com.handyhive.backend.controller;

import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Service;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.ServiceRepository;
import com.handyhive.backend.service.ProviderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;
    private final ProviderRepository providerRepository;
    private final ServiceRepository serviceRepository;

    public ProviderController(ProviderService providerService, ProviderRepository providerRepository, ServiceRepository serviceRepository) {
        this.providerService = providerService;
        this.providerRepository = providerRepository;
        this.serviceRepository = serviceRepository;
    }

    // ✅ 1. Register Provider (Handles Files + Skills)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerProvider(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone,
            @RequestParam(value = "serviceIds", required = false) List<Long> serviceIds,
            @RequestParam(value = "document", required = false) MultipartFile document
    ) {
        try {
            Provider provider = new Provider();
            provider.setFirstName(firstName);
            provider.setLastName(lastName);
            provider.setEmail(email);
            provider.setPasswordHash(password);
            provider.setPhoneNumber(phone);
            provider.setBio("New Service Provider");

            // Logic: Link Selected Services (Skills)
            if (serviceIds != null && !serviceIds.isEmpty()) {
                List<Service> selectedServices = serviceRepository.findAllById(serviceIds);
                provider.setServices(selectedServices);
            }

            // Pass the provider + file to the service
            Provider created = providerService.registerProviderWithDoc(provider, document);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // 2. Get Provider Profile
    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.getProviderById(id));
    }

    // 3. Update Profile Details
    @PutMapping("/{id}")
    public ResponseEntity<Provider> updateProvider(@PathVariable Long id, @RequestBody Provider updatedData) {
        return ResponseEntity.ok(providerService.updateProviderDetails(id, updatedData));
    }

    // 4. Change Password
    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> passwords) {
        try {
            providerService.changePassword(id, passwords.get("oldPassword"), passwords.get("newPassword"));
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 5. Update Subscription
    @PutMapping("/{id}/subscription")
    public ResponseEntity<?> updateSubscription(@PathVariable Long id, @RequestParam Integer planId) {
        Provider provider = providerService.getProviderById(id);
        provider.setSubscriptionId(planId);
        // Auto-verify if they buy a premium plan (Plan ID > 1)
        if (planId > 1) provider.setIsVetted(true);

        providerRepository.save(provider);
        return ResponseEntity.ok("Subscription updated to Plan " + planId);
    }

    // 6. Delete Account
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvider(@PathVariable Long id) {
        providerRepository.deleteById(id);
        return ResponseEntity.ok("Account deleted successfully");
    }

    // ✅ 7. Match Provider for Cart
    @GetMapping("/match/{serviceId}")
    public ResponseEntity<?> findProviderForService(@PathVariable Long serviceId) {
        List<Provider> providers = providerRepository.findByServiceId(serviceId);

        if (providers.isEmpty()) {
            return ResponseEntity.status(404).body("No providers found for this service.");
        }

        // Return the first available provider
        return ResponseEntity.ok(providers.get(0));
    }
}