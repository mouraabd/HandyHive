package com.handyhive.backend;

import com.handyhive.backend.model.Customer;
import com.handyhive.backend.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindByEmailIgnoreCase() {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("User"); // ✅ Required
        customer.setEmail("TEST@EMAIL.COM");
        customer.setPasswordHash("123456"); // ✅ Required
        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByEmailIgnoreCase("test@email.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("TEST@EMAIL.COM");
    }

    @Test
    public void testCustomerExists() {
        Customer c = new Customer();
        c.setFirstName("Exists"); // ✅ Required
        c.setLastName("Person");  // ✅ Required
        c.setEmail("exists@test.com");
        c.setPasswordHash("pass"); // ✅ Required
        customerRepository.save(c);

        Customer found = customerRepository.findByEmail("exists@test.com");
        assertThat(found).isNotNull();
    }
}