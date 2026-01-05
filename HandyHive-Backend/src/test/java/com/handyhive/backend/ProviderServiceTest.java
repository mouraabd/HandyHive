package com.handyhive.backend;

import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.Job;
import com.handyhive.backend.model.Provider;
import com.handyhive.backend.model.Service;
import com.handyhive.backend.repository.*;
import com.handyhive.backend.service.ProviderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock private ProviderRepository providerRepository;
    @Mock private ServiceRepository serviceRepository;
    @Mock private JobRepository jobRepository;

    // Extra mocks (safe even if ProviderService doesn't use them)
    @Mock private RatingRepository ratingRepository;
    @Mock private SubscriptionRepository subscriptionRepository;
    @Mock private UserRepository userRepository;

    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProviderService providerService;

    @Test
    void matchProviderForService_shouldReturnOneOfAvailableProviders() {
        Long serviceId = 1L;

        Service svc = new Service();
        svc.setServiceId(serviceId);
        svc.setName("Plumbing");
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(svc));

        Provider p1 = new Provider();
        p1.setProviderId(10L);
        p1.setAvgRating(4.9);
        p1.setJobsCompleted(50);
        p1.setIsVetted(true);

        Provider p2 = new Provider();
        p2.setProviderId(11L);
        p2.setAvgRating(4.7);
        p2.setJobsCompleted(1);
        p2.setIsVetted(true);

        // Your ProviderService might call either of these repository methods.
        // lenient() prevents UnnecessaryStubbingException for the unused one.
        lenient().when(providerRepository.findByServices_ServiceId(serviceId)).thenReturn(List.of(p1, p2));
        lenient().when(providerRepository.findByServiceId(serviceId)).thenReturn(List.of(p1, p2));

        // If your matching uses current workload, this makes it deterministic-ish
        lenient().when(jobRepository.findByProvider_ProviderId(10L)).thenReturn(List.of(new Job(), new Job()));
        lenient().when(jobRepository.findByProvider_ProviderId(11L)).thenReturn(List.of());

        Provider chosen = providerService.matchProviderForService(serviceId, false);

        assertThat(chosen).isNotNull();
        // Don’t assert a single ID unless your algorithm is guaranteed deterministic.
        assertThat(chosen.getProviderId()).isIn(10L, 11L);
    }

    @Test
    void matchProviderForService_shouldThrow_whenNoProviders() {
        Long serviceId = 1L;

        Service svc = new Service();
        svc.setServiceId(serviceId);
        svc.setName("Plumbing");
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(svc));

        lenient().when(providerRepository.findByServices_ServiceId(serviceId)).thenReturn(List.of());
        lenient().when(providerRepository.findByServiceId(serviceId)).thenReturn(List.of());

        assertThatThrownBy(() -> providerService.matchProviderForService(serviceId, false))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
