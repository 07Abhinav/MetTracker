package com.metracker.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_medicines")
@Getter
@Setter
public class UserMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false) // changes
    private Medicine medicine;
    private String medicineName;
    private String description;
    private String manufacturer;
    private LocalDate expiryDate;
    private boolean donated=false;
}
