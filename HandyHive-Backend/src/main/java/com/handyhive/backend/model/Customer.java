package com.handyhive.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String passwordHash;

    private String phoneNumber;
    private String role;
    private OffsetDateTime registrationDate;
    private Integer subscriptionId;

    // --- FIX: Prevents Infinite Loop (Customer -> Job -> Customer...) ---
    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Job> jobs;
}