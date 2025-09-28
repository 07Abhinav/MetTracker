package com.metracker.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pharmacy_stocks")
@Getter
@Setter
public class PharmacyStock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User pharmacy;
    @ManyToOne
    private Medicine medicine;
    private Integer quantity;
    private LocalDate expiryDate;
}
