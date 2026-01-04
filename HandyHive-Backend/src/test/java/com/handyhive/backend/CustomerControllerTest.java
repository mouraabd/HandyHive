package com.handyhive.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handyhive.backend.model.Customer;
import com.handyhive.backend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // ✅ New Spring Boot 3.4 annotation
// If the line above is red, use: import org.springframework.boot.test.mock.mockito.MockBean; and change @MockitoBean to @MockBean below.

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // ✅ Loads full context (Fixes missing bean errors)
@AutoConfigureMockMvc // ✅ Enables MockMvc for testing endpoints
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // ✅ Replaces deprecated @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterCustomer_Success() throws Exception {
        Customer customer = new Customer();
        customer.setFirstName("Alice");
        customer.setEmail("alice@test.com");
        customer.setPasswordHash("password123");

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setFirstName("Alice");
        savedCustomer.setEmail("alice@test.com");

        when(customerService.registerCustomer(any(Customer.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testRegisterCustomer_DuplicateEmail_Returns409() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("duplicate@test.com");

        when(customerService.registerCustomer(any(Customer.class)))
                .thenThrow(new RuntimeException("Conflict: Email already taken"));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}