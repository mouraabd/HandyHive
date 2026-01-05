package com.handyhive.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handyhive.backend.dto.DeleteAccountDTO;
import com.handyhive.backend.dto.UserUpdateDTO;
import com.handyhive.backend.model.User;
import com.handyhive.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_returns201() throws Exception {
        User saved = new User();
        saved.setUserId(1L);
        saved.setEmail("user@test.com");
        saved.setFirstName("U");
        saved.setLastName("Test");

        when(userService.registerUser(any(User.class))).thenReturn(saved);

        User req = new User();
        req.setEmail("user@test.com");
        req.setPasswordHash("pw");
        req.setFirstName("U");
        req.setLastName("Test");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    @Test
    void searchUserByEmail_notFound_returns404() throws Exception {
        when(userService.findByEmail(eq("missing@test.com"))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/search").param("email", "missing@test.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "tester")
    void updateUser_returns200() throws Exception {
        User updated = new User();
        updated.setUserId(1L);
        updated.setFirstName("NewName");

        when(userService.updateUser(eq(1L), any(UserUpdateDTO.class))).thenReturn(updated);

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setFirstName("NewName");

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.firstName").value("NewName"));
    }

    @Test
    @WithMockUser(username = "tester")
    void deleteUserSecure_wrongPassword_returns401() throws Exception {
        doThrow(new IllegalArgumentException("wrong password"))
                .when(userService).deleteUserSecurely(eq(1L), eq("wrong"));

        DeleteAccountDTO dto = new DeleteAccountDTO();
        dto.setPassword("wrong");

        mockMvc.perform(delete("/api/users/1/secure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}
