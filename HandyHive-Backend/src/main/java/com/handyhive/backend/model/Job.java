package com.handyhive.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅ Added to prevent loops
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @OneToOne(mappedBy = "job", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnoreProperties("job")
    private Rating rating;
}