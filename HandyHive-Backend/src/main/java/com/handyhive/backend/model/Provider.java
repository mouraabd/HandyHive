package com.handyhive.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerId;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String passwordHash;

    private String phoneNumber;
    private String bio;

    private Boolean isVetted = false;
    private Integer subscriptionId = 1;

    private String role; // "PROVIDER"
    private Double avgRating = 0.0;

    // ✅ ADD THIS FIELD (Maps to 'jobs_completed' in DB)
    private Integer jobsCompleted = 0;

    private String vettingDocPath;

    @ManyToMany
    @JoinTable(
            name = "provider_services",
            joinColumns = @JoinColumn(name = "provider_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services;

    @OneToMany(mappedBy = "provider")
    @JsonIgnore
    private List<Job> jobs;
}