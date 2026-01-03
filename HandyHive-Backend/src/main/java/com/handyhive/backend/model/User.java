package com.handyhive.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long userId;

    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String email;

    @Column(nullable = false)
    @Getter @Setter
    private String passwordHash;

    @Getter @Setter
    private String phoneNumber;

    @Getter @Setter
    private String role;

    @Getter @Setter
    private OffsetDateTime registrationDate;

    @Getter @Setter
    private Integer subscriptionId;

    // --- MANUAL SETTERS/GETTERS TO FIX ERROR ---

    // Sometimes Lombok fails on specific fields if IDE plugins aren't synced.
    // Adding these manually fixes it instantly.

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
}