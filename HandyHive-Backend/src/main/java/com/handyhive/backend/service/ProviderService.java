package com.handyhive.backend.service;

import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.JobStatus;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Service;
import com.handyhive.backend.repository.JobRepository;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.repository.ServiceRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@org.springframework.stereotype.Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ServiceRepository serviceRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "uploads/";

    public ProviderService(ProviderRepository providerRepository,
                           ServiceRepository serviceRepository,
                           JobRepository jobRepository,
                           PasswordEncoder passwordEncoder) {
        this.providerRepository = providerRepository;
        this.serviceRepository = serviceRepository;
        this.jobRepository = jobRepository;
        this.passwordEncoder = passwordEncoder;
        new File(UPLOAD_DIR).mkdirs();
    }

    public List<Service> requireServicesByIds(List<Long> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) return Collections.emptyList();
        List<Service> found = serviceRepository.findAllById(serviceIds);
        if (found.size() != serviceIds.size()) {
            throw new IllegalArgumentException("One or more serviceIds do not exist");
        }
        return found;
    }

    public Provider registerProviderWithDoc(Provider provider, MultipartFile document) throws IOException {
        if (providerRepository.findByEmail(provider.getEmail()) != null
                || providerRepository.findByEmailIgnoreCase(provider.getEmail()).isPresent()) {
            throw new RuntimeException("Conflict: Email already taken");
        }

        if (document != null && !document.isEmpty()) {
            String original = Objects.toString(document.getOriginalFilename(), "document");
            String fileName = System.currentTimeMillis() + "_" + original;
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(document.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            provider.setVettingDocPath(path.toString());
        }

        provider.setPhoneNumber(normalizeCzechPhone(provider.getPhoneNumber()));
        provider.setPasswordHash(passwordEncoder.encode(provider.getPasswordHash()));
        provider.setRole("PROVIDER");
        if (provider.getSubscriptionId() == null) provider.setSubscriptionId(1);

        return providerRepository.save(provider);
    }

    public Provider registerProvider(Provider provider) {
        try {
            return registerProviderWithDoc(provider, null);
        } catch (IOException e) {
            throw new RuntimeException("Error registering provider", e);
        }
    }

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    public Provider getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + id));
    }

    public Provider updateProviderDetails(Long id, Provider update) {
        Provider existing = getProviderById(id);
        if (update.getFirstName() != null) existing.setFirstName(update.getFirstName());
        if (update.getLastName() != null) existing.setLastName(update.getLastName());
        if (update.getPhoneNumber() != null) existing.setPhoneNumber(normalizeCzechPhone(update.getPhoneNumber()));
        if (update.getBio() != null) existing.setBio(update.getBio());
        return providerRepository.save(existing);
    }

    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        Provider provider = getProviderById(id);
        if (oldPassword == null || newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }
        if (!passwordEncoder.matches(oldPassword, provider.getPasswordHash())) {
            throw new IllegalArgumentException("Incorrect current password");
        }
        provider.setPasswordHash(passwordEncoder.encode(newPassword));
        providerRepository.save(provider);
    }

    @Transactional
    public Provider updateSubscription(Long providerId, Integer planId) {
        Provider provider = getProviderById(providerId);
        provider.setSubscriptionId(planId);

        if (planId != null && planId > 1) provider.setIsVetted(true);
        else if (planId != null && planId == 1) provider.setIsVetted(false);

        return providerRepository.save(provider);
    }

    @Transactional
    public void deleteProvider(Long id) {
        Provider provider = getProviderById(id);
        jobRepository.deleteAll(jobRepository.findByProvider_ProviderId(id));
        if (provider.getServices() != null) {
            provider.getServices().clear();
        }
        providerRepository.delete(provider);
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

    public List<Provider> recommendProvidersForService(Long serviceId) {
        return providerRepository.recommendProvidersForService(serviceId);
    }

    // M:N CRUD
    public List<Service> getProviderServices(Long providerId) {
        Provider provider = getProviderById(providerId);
        return provider.getServices() == null ? List.of() : provider.getServices();
    }

    @Transactional
    public List<Service> replaceProviderServices(Long providerId, List<Long> serviceIds) {
        Provider provider = getProviderById(providerId);
        List<Service> services = requireServicesByIds(serviceIds);
        provider.setServices(new ArrayList<>(services));
        Provider saved = providerRepository.save(provider);
        return saved.getServices() == null ? List.of() : saved.getServices();
    }

    @Transactional
    public void addServiceToProvider(Long providerId, Long serviceId) {
        Provider provider = getProviderById(providerId);
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        if (provider.getServices() == null) provider.setServices(new ArrayList<>());

        boolean alreadyAssigned = provider.getServices().stream()
                .anyMatch(s -> Objects.equals(s.getServiceId(), serviceId));

        if (!alreadyAssigned) {
            provider.getServices().add(service);
            providerRepository.save(provider);
        }
    }

    @Transactional
    public void removeServiceFromProvider(Long providerId, Long serviceId) {
        Provider provider = getProviderById(providerId);
        if (provider.getServices() == null || provider.getServices().isEmpty()) return;
        provider.getServices().removeIf(s -> Objects.equals(s.getServiceId(), serviceId));
        providerRepository.save(provider);
    }

    // ✅ SMART MATCH + FALLBACK
    @Transactional(readOnly = true)
    public Provider matchProviderForService(Long serviceId, boolean urgent) {

        // Ensure service exists (helps debugging)
        Service svc = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        // 1) Normal (correct) way: via M:N join table
        List<Provider> candidates = providerRepository.findByServices_ServiceId(serviceId);

        // 2) Fallback for YOUR CURRENT DB: infer from provider bio/name/email if join table not populated
        if (candidates.isEmpty()) {
            String serviceName = (svc.getName() == null ? "" : svc.getName()).toLowerCase(Locale.ROOT);

            Set<String> keywords = new LinkedHashSet<>();
            keywords.add(serviceName);

            // simple synonyms so your existing "Master Plumber" etc work
            if (serviceName.contains("plumb")) keywords.add("plumb");
            if (serviceName.contains("electric")) { keywords.add("electric"); keywords.add("spark"); }
            if (serviceName.contains("clean")) keywords.add("clean");
            if (serviceName.contains("handy")) { keywords.add("handy"); keywords.add("repair"); }

            candidates = providerRepository.findAll().stream()
                    // providers with no services assigned are the common “current DB” case
                    .filter(p -> p.getServices() == null || p.getServices().isEmpty())
                    .filter(p -> {
                        String hay = ((p.getBio() == null ? "" : p.getBio()) + " " +
                                (p.getFirstName() == null ? "" : p.getFirstName()) + " " +
                                (p.getLastName() == null ? "" : p.getLastName()) + " " +
                                (p.getEmail() == null ? "" : p.getEmail()))
                                .toLowerCase(Locale.ROOT);

                        for (String k : keywords) {
                            if (!k.isBlank() && hay.contains(k)) return true;
                        }
                        return false;
                    })
                    .toList();
        }

        if (candidates.isEmpty()) {
            throw new ResourceNotFoundException("No providers found for service: " + svc.getName());
        }

        // urgent => prefer vetted (fallback to all)
        if (urgent) {
            List<Provider> vetted = candidates.stream()
                    .filter(p -> Boolean.TRUE.equals(p.getIsVetted()))
                    .toList();
            if (!vetted.isEmpty()) candidates = vetted;
        }

        // load balance by active jobs
        Collection<JobStatus> active = List.of(JobStatus.PENDING, JobStatus.IN_PROGRESS);
        Map<Long, Long> load = new HashMap<>();
        for (Provider p : candidates) {
            long cnt = jobRepository.countByProvider_ProviderIdAndStatusIn(p.getProviderId(), active);
            load.put(p.getProviderId(), cnt);
        }

        long minLoad = load.values().stream().min(Long::compareTo).orElse(0L);

        List<Provider> leastLoaded = candidates.stream()
                .filter(p -> Objects.equals(load.get(p.getProviderId()), minLoad))
                .sorted(Comparator
                        .comparing((Provider p) -> Boolean.TRUE.equals(p.getIsVetted())).reversed()
                        .thenComparing(Provider::getAvgRating, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Provider::getJobsCompleted, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Provider::getProviderId)
                )
                .toList();

        // random among top 3
        int poolSize = Math.min(3, leastLoaded.size());
        int pick = ThreadLocalRandom.current().nextInt(poolSize);
        return leastLoaded.get(pick);
    }
}
