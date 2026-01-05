package com.handyhive.backend;

import com.handyhive.backend.model.User;
import com.handyhive.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_returnsUser() {
        User u = new User();
        u.setEmail("repo@test.com");
        u.setPasswordHash("hash");
        u.setFirstName("Repo");
        u.setLastName("User");
        u.setRole("ROLE_USER");
        u.setRegistrationDate(OffsetDateTime.now());

        userRepository.save(u);

        assertThat(userRepository.findByEmail("repo@test.com"))
                .isPresent()
                .get()
                .extracting(User::getFirstName)
                .isEqualTo("Repo");
    }
}
