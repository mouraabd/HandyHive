package com.handyhive.backend;

import com.handyhive.backend.model.Provider;
import com.handyhive.backend.repository.ProviderRepository;
import com.handyhive.backend.service.ProviderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProviderService providerService;

    @Test
    void testRegisterProvider_ShouldHashPassword() {
        // 1. Arrange
        Provider inputProvider = new Provider();
        inputProvider.setEmail("test@provider.com");
        inputProvider.setPasswordHash("plainPassword123");

        // Mock Password Encoder
        when(passwordEncoder.encode("plainPassword123")).thenReturn("hashedPassword");

        // Mock Repository to return the provider (acting as if it saved it)
        when(providerRepository.save(any(Provider.class))).thenAnswer(i -> i.getArguments()[0]);

        // Mock check for existing email (return null = email not taken)
        when(providerRepository.findByEmail("test@provider.com")).thenReturn(null);

        // 2. Act
        Provider result = providerService.registerProvider(inputProvider);

        // 3. Assert
        assertNotNull(result);
        assertEquals("test@provider.com", result.getEmail());
        assertEquals("hashedPassword", result.getPasswordHash()); // Should be hashed now
        assertEquals("PROVIDER", result.getRole()); // Role should be set automatically

        verify(passwordEncoder).encode("plainPassword123");
    }
}