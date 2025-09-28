package com.metracker.backend.controller;

import com.metracker.backend.model.*;
import com.metracker.backend.repository.MedicineRepository;
import com.metracker.backend.repository.PharmacyStockRepository;
import com.metracker.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.metracker.backend.dto.PharmacyStockRequest;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;
    private final PharmacyStockRepository stockRepo;

    public PharmacyController(UserRepository userRepository, MedicineRepository medicineRepository, PharmacyStockRepository stockRepo) {
        this.userRepository = userRepository;
        this.medicineRepository = medicineRepository;
        this.stockRepo = stockRepo;
    }

    @PostMapping("/stock")
    public ResponseEntity<?> addOrUpdateStock(@AuthenticationPrincipal UserDetails principal, @RequestBody PharmacyStockRequest req) {
        User pharmacy = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(pharmacy.getRole() != Role.PHARMACY) {
            return ResponseEntity.status(403).body("Only pharmacies can add stock");
        }
        Medicine med;
        if(req.medicineId!=null) {
            med = medicineRepository.findById(req.medicineId).orElseThrow(() -> new RuntimeException("Medicine not found"));
        }
        else {
            med = medicineRepository.findByNameIgnoreCase(req.medicineName)
                    .orElseGet(() -> {
                        Medicine m= new Medicine();
                        m.setName(req.medicineName);
                        return medicineRepository.save(m);
                    });
        }

        PharmacyStock stock = new PharmacyStock();
        stock.setPharmacy(pharmacy);
        stock.setMedicine(med);
        stock.setQuantity(req.quantity);
        stock.setExpiryDate(req.expiryDate);
        stockRepo.save(stock);
        return ResponseEntity.ok("Stock updated");
    }
}
