package com.metracker.backend.model;

import com.metracker.backend.model.DonationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Getter
@Setter
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User donor;

    @ManyToOne
    private Medicine medicine;

    @ManyToOne
    private User ngo;

    @Enumerated(EnumType.STRING)
    private DonationStatus status=DonationStatus.PENDING;

    private LocalDateTime createdAt=LocalDateTime.now();
}
