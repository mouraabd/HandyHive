package com.handyhive.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅ Added to prevent loops
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    private String serviceName;
    private String description;
    private Boolean isUrgent = false;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private OffsetDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    // ✅ ADD THIS FIELD TO FIX THE ERROR
    @OneToOne(mappedBy = "job")
    @JsonIgnoreProperties("job") // Prevents infinite recursion
    private Rating rating;
}