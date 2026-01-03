package com.handyhive.backend.service;

import com.handyhive.backend.dto.UserUpdateDTO;
import com.handyhive.backend.exception.ResourceNotFoundException;
import com.handyhive.backend.model.User;
import com.handyhive.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // Constructor injection ensures the correct PasswordEncoder bean is used
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        System.out.println("--- UserService: Constructor called, PasswordEncoder injected: " + passwordEncoder.getClass().getName() + " ---");
    }
    // --- ADD THIS METHOD ---
    public java.util.Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User registerUser(User user) {
        // Hash the plain-text password before saving
        System.out.println("--- UserService: Hashing password for user: " + user.getEmail() + " ---");
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        System.out.println("--- UserService: Hashed password: " + user.getPasswordHash() + " ---");

        // Set default values
        user.setRole("ROLE_USER");
        user.setRegistrationDate(OffsetDateTime.now());

        return userRepository.save(user);
    }

    /**
     * Finds a single user by their ID.
     * @param id The ID of the user to find.
     * @return The found User.
     * @throws ResourceNotFoundException if no user is found with that ID.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * Updates an existing user's details.
     * @param id The ID of the user to update.
     * @param userUpdateDTO The DTO containing the new data.
     * @return The updated User.
     * @throws ResourceNotFoundException if no user is found with that ID.
     */
    @Transactional
    public User updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User existingUser = getUserById(id);

        if (userUpdateDTO.getFirstName() != null) existingUser.setFirstName(userUpdateDTO.getFirstName());
        if (userUpdateDTO.getLastName() != null) existingUser.setLastName(userUpdateDTO.getLastName());
        if (userUpdateDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(userUpdateDTO.getPhoneNumber());

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isBlank()) {
            existingUser.setPasswordHash(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        // Save Subscription
        if (userUpdateDTO.getSubscriptionId() != null) {
            existingUser.setSubscriptionId(userUpdateDTO.getSubscriptionId());
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUserSecurely(Long id, String password) {
        User user = getUserById(id);

        // Check if password matches
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        userRepository.delete(user);
    }

    /**
     * Deletes a user from the database.
     * @param id The ID of the user to delete.
     * @throws ResourceNotFoundException if no user is found with that ID.
     */
    @Transactional
    public void deleteUser(Long id) {
        // First, check if the user exists (this will throw 404 if not)
        User user = getUserById(id);

        // If the user exists, delete them
        userRepository.delete(user);
    }


}