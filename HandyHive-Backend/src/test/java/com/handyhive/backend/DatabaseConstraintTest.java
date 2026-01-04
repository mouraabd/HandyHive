package com.handyhive.backend;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class DatabaseConstraintTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testDuplicateEmailThrowsDataIntegrityException() {
        // 1. Save first customer (Fully Populated)
        Customer c1 = new Customer();
        c1.setEmail("unique@test.com");
        c1.setFirstName("First");
        c1.setLastName("User");      // ✅ Required
        c1.setPasswordHash("hash1"); // ✅ Required
        customerRepository.saveAndFlush(c1);

        // 2. Save second customer with SAME email
        Customer c2 = new Customer();
        c2.setEmail("unique@test.com"); // Duplicate!
        c2.setFirstName("Second");
        c2.setLastName("User");      // ✅ Required
        c2.setPasswordHash("hash2"); // ✅ Required

        // 3. Assert that the DATABASE (Hibernate) rejects it
        assertThrows(DataIntegrityViolationException.class, () -> {
            customerRepository.saveAndFlush(c2);
        });
    }
}