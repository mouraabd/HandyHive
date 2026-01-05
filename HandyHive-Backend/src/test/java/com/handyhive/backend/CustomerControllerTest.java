package com.handyhive.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handyhive.backend.model.Customer;
import com.handyhive.backend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    public void testRegisterCustomer_Success_Returns201() throws Exception {
        Customer saved = new Customer();
        saved.setId(1L);
        saved.setEmail("new@test.com");

        when(customerService.registerCustomer(any(Customer.class))).thenReturn(saved);

        Customer req = new Customer();
        req.setEmail("new@test.com");
        req.setPasswordHash("plainPass");

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/customers/1")));
    }

    @Test
    public void testRegisterCustomer_DuplicateEmail_Returns409() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("duplicate@test.com");
        customer.setPasswordHash("x");

        when(customerService.registerCustomer(any(Customer.class)))
                .thenThrow(new RuntimeException("Conflict: Email already taken"));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}
