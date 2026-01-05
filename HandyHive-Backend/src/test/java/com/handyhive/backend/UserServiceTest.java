package com.handyhive.backend;

import com.handyhive.backend.dto.UserUpdateDTO;
import com.handyhive.backend.model.User;
import com.handyhive.backend.repository.UserRepository;
import com.handyhive.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    @Test
    void registerUser_hashesPassword_setsDefaults_saves() {
        User req = new User();
        req.setEmail("user@test.com");
        req.setPasswordHash("plain");
        req.setFirstName("U");
        req.setLastName("Test");

        when(passwordEncoder.encode("plain")).thenReturn("hashed");

        User saved = new User();
        saved.setUserId(1L);
        saved.setEmail("user@test.com");
        saved.setPasswordHash("hashed");
        saved.setRole("ROLE_USER");

        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.registerUser(req);

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getPasswordHash()).isEqualTo("hashed");
        assertThat(result.getRole()).isEqualTo("ROLE_USER");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User toSave = captor.getValue();

        assertThat(toSave.getPasswordHash()).isEqualTo("hashed");
        assertThat(toSave.getRole()).isEqualTo("ROLE_USER");
        assertThat(toSave.getRegistrationDate()).isNotNull();
    }

    @Test
    void updateUser_updatesFields_andHashesNewPassword_whenProvided() {
        User existing = new User();
        existing.setUserId(5L);
        existing.setEmail("u@test.com");
        existing.setPasswordHash("old-hash");
        existing.setFirstName("Old");
        existing.setLastName("Name");

        when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newPass")).thenReturn("new-hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setFirstName("New");
        dto.setPassword("newPass");
        dto.setSubscriptionId(2);

        User updated = userService.updateUser(5L, dto);

        assertThat(updated.getFirstName()).isEqualTo("New");
        assertThat(updated.getPasswordHash()).isEqualTo("new-hash");
        assertThat(updated.getSubscriptionId()).isEqualTo(2);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUserSecurely_wrongPassword_throws_andDoesNotDelete() {
        User existing = new User();
        existing.setUserId(9L);
        existing.setPasswordHash("hash");

        when(userRepository.findById(9L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUserSecurely(9L, "wrong"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid password");

        verify(userRepository, never()).delete(any());
    }
}
