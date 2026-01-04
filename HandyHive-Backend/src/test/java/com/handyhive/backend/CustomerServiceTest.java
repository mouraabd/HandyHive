package com.handyhive.backend;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.repository.CustomerRepository;
import com.handyhive.backend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void testRegister_DuplicateEmailIgnoreCase_ThrowsException() {
        Customer newCustomer = new Customer();
        newCustomer.setEmail("Jack@Hero.com"); // Uppercase Input

        // Mock DB finding "jack@hero.com" (Lowercase existing)
        Customer existing = new Customer();
        existing.setEmail("jack@hero.com");

        when(customerRepository.findByEmailIgnoreCase("Jack@Hero.com"))
                .thenReturn(Optional.of(existing));

        // Assert that it throws RuntimeException
        assertThrows(RuntimeException.class, () -> {
            customerService.registerCustomer(newCustomer);
        });
    }
}