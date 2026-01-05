package com.handyhive.backend.controller;

import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Service;
import com.handyhive.backend.service.ProviderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/providers")
@CrossOrigin(origins = "*")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    // ✅ CREATE Provider (201 Created)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Provider> registerProvider(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam(value = "bio", required = false) String bio,

            // accept multiple formats for service IDs
            @RequestParam(value = "serviceIds", required = false) List<Long> serviceIds,
            @RequestParam(value = "serviceIds[]", required = false) List<Long> serviceIdsBracket,
            @RequestParam(value = "serviceIdsCsv", required = false) String serviceIdsCsv,

            @RequestPart(value = "document", required = false) MultipartFile document
    ) throws IOException {

        // Merge serviceIds variants safely
        Set<Long> mergedIds = new LinkedHashSet<>();
        if (serviceIds != null) mergedIds.addAll(serviceIds);
        if (serviceIdsBracket != null) mergedIds.addAll(serviceIdsBracket);
        if (serviceIdsCsv != null && !serviceIdsCsv.isBlank()) {
            List<Long> parsed = Arrays.stream(serviceIdsCsv.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .map(Long::valueOf)
                    .toList();
            mergedIds.addAll(parsed);
        }

        Provider provider = new Provider();
        provider.setFirstName(firstName);
        provider.setLastName(lastName);
        provider.setEmail(email);
        provider.setPasswordHash(password);
        provider.setPhoneNumber(phone);
        provider.setBio(bio != null ? bio : "New Service Provider");

        if (!mergedIds.isEmpty()) {
            List<Service> services = providerService.requireServicesByIds(new ArrayList<>(mergedIds));
            provider.setServices(services);
        }

        Provider savedProvider = providerService.registerProviderWithDoc(provider, document);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProvider.getProviderId())
                .toUri();

        return ResponseEntity.created(location).body(savedProvider);
    }

    // ✅ READ (list)
    @GetMapping
    public ResponseEntity<List<Provider>> getAllProviders() {
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    // ✅ READ (by id)
    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.getProviderById(id));
    }

    // ✅ COMPLEX QUERY endpoint (kept for rubric)
    @GetMapping("/recommendations")
    public ResponseEntity<List<Provider>> recommendProviders(@RequestParam Long serviceId) {
        return ResponseEntity.ok(providerService.recommendProvidersForService(serviceId));
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Provider> updateProvider(@PathVariable Long id, @RequestBody Provider update) {
        return ResponseEntity.ok(providerService.updateProviderDetails(id, update));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        providerService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok("Password updated successfully!");
    }

    @PutMapping("/{id}/subscription")
    public ResponseEntity<Provider> updateSubscription(
            @PathVariable Long id,
            @RequestParam Integer planId
    ) {
        return ResponseEntity.ok(providerService.updateSubscription(id, planId));
    }

    // ✅ DELETE (204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ M:N association CRUD
    @GetMapping("/{providerId}/services")
    public ResponseEntity<List<Service>> getProviderServices(@PathVariable Long providerId) {
        return ResponseEntity.ok(providerService.getProviderServices(providerId));
    }

    @PutMapping("/{providerId}/services")
    public ResponseEntity<List<Service>> replaceProviderServices(
            @PathVariable Long providerId,
            @RequestBody List<Long> serviceIds
    ) {
        return ResponseEntity.ok(providerService.replaceProviderServices(providerId, serviceIds));
    }

    @PutMapping("/{providerId}/services/{serviceId}")
    public ResponseEntity<Void> addServiceToProvider(
            @PathVariable Long providerId,
            @PathVariable Long serviceId
    ) {
        providerService.addServiceToProvider(providerId, serviceId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{providerId}/services/{serviceId}")
    public ResponseEntity<Void> removeServiceFromProvider(
            @PathVariable Long providerId,
            @PathVariable Long serviceId
    ) {
        providerService.removeServiceFromProvider(providerId, serviceId);
        return ResponseEntity.noContent().build();
    }

    // ✅ SMART MATCH endpoint used by checkout
    @GetMapping("/match/{serviceId}")
    public ResponseEntity<Provider> findProviderForService(
            @PathVariable Long serviceId,
            @RequestParam(name = "urgent", defaultValue = "false") boolean urgent
    ) {
        return ResponseEntity.ok(providerService.matchProviderForService(serviceId, urgent));
    }
}
