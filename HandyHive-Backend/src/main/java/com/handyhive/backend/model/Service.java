package com.handyhive.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    private String name;
    private String description;
    private Double basePrice;

    // ✅ CRITICAL FIX: Add @JsonIgnore here
    // This stops the Provider -> Service -> Provider infinite loop
    @ManyToMany(mappedBy = "services")
    @JsonIgnore
    private List<Provider> providers;
}