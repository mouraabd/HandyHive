package com.handyhive.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "ratingId") // <--- FIX: This stops the loop automatically
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @Column(nullable = false)
    private Double score;

    @Column(length = 500)
    private String comment;

    @OneToOne
    @JoinColumn(name = "job_id", nullable = false, unique = true)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;
}