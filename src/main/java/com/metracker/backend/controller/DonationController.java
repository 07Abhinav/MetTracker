package com.metracker.backend.controller;

import com.metracker.backend.model.Donation;
import com.metracker.backend.model.Medicine;
import com.metracker.backend.model.*;
import com.metracker.backend.repository.DonationRepository;
import com.metracker.backend.repository.MedicineRepository;
import com.metracker.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/donations")
public class DonationController {
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;

    public DonationController(DonationRepository donationRepository, UserRepository userRepository, MedicineRepository medicineRepository) {
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.medicineRepository = medicineRepository;
    }

    @PostMapping("/donate")
    public ResponseEntity<?> donate(@AuthenticationPrincipal UserDetails principal,
                                    @RequestParam Long medicineId,
                                    @RequestParam(required = false) Long ngoId) {
        User donor = userRepository.findByEmail(principal.getUsername()).orElseThrow();
        Medicine med = medicineRepository.findById(medicineId).orElseThrow();
        Donation donate = new Donation();
        donate.setDonor(donor);
        donate.setMedicine(med);
        if (ngoId != null) {
            User ngo = userRepository.findById(ngoId).orElseThrow();
            donate.setNgo(ngo);
        }
        donate.setStatus(DonationStatus.PENDING);
        donationRepository.save(donate);
        return ResponseEntity.ok("Donation request created successfully " + donate);
    }
}
