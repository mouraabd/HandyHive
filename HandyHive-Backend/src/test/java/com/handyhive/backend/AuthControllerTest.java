package com.handyhive.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handyhive.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void login_success_returns200Json() throws Exception {
        when(authService.login(eq("a@test.com"), eq("pass")))
                .thenReturn(Map.of(
                        "email", "a@test.com",
                        "token", "dummy-token-12345",
                        "role", "CUSTOMER",
                        "id", 1,
                        "name", "A B"
                ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "a@test.com",
                                "password", "pass"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void login_missingFields_returns400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "",
                                "password", ""
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing email or password"));
    }

    @Test
    void login_profileNotFound_returns401() throws Exception {
        when(authService.login(eq("x@test.com"), eq("pass")))
                .thenThrow(new IllegalArgumentException("Profile not found. Please register."));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "x@test.com",
                                "password", "pass"
                        ))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Profile not found. Please register."));
    }

    @Test
    void login_invalidCredentials_returns401() throws Exception {
        when(authService.login(eq("x@test.com"), eq("wrong")))
                .thenThrow(new RuntimeException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "x@test.com",
                                "password", "wrong"
                        ))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }
}
