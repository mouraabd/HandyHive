package com.handyhive.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Import this

import java.math.BigDecimal;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor // Lombok generates a default constructor needed by JPA
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Integer id;

    @Column(name = "plan_name", nullable = false, length = 50)
    private String planName;

    @Column(name = "price_czk", nullable = false)
    private BigDecimal priceCzk; // This is BigDecimal

    @Column(columnDefinition = "TEXT")
    private String description;

    // MANUAL CONSTRUCTOR
    // We need to ensure the argument 'priceCzk' matches the field type
    public Subscription(String planName, BigDecimal priceCzk, String description) {
        this.planName = planName;
        this.priceCzk = priceCzk;
        this.description = description;
    }
}