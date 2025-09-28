package com.metracker.backend.service;

import com.metracker.backend.dto.AddUserMedicineRequest;
import com.metracker.backend.model.*;
import com.metracker.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;
    private final UserMedicineRepository userMedicineRepository;

    public MedicineService(MedicineRepository medicineRepository, UserRepository userRepository, UserMedicineRepository userMedicineRepository) {
        this.medicineRepository = medicineRepository;
        this.userRepository = userRepository;
        this.userMedicineRepository = userMedicineRepository;
    }

    @Transactional
    public UserMedicine addUserMedicine(Long userId, AddUserMedicineRequest request) {
        Medicine med = medicineRepository.findByNameIgnoreCase(request.getMedicineName())
                .orElseGet(() -> {
                    Medicine newMed = new Medicine();
                    newMed.setName(request.getMedicineName());
                    newMed.setDescription(request.getDescription());
                    newMed.setManufacturer(request.getManufacturer());
                    newMed.setDosage(request.getDosage());
                    newMed.setExpiryDate(request.getExpiryDate());
                    return medicineRepository.save(newMed);
                });

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        UserMedicine um = new UserMedicine();
        um.setUser(user);
        um.setMedicine(med);
        um.setExpiryDate(request.getExpiryDate());
        um.setDonated(false);
        return userMedicineRepository.save(um);
    }

    public List<UserMedicine> listUserMedicines(Long userId){
        return userMedicineRepository.findByUserId(userId);
    }

    public List<UserMedicine> findExpringBetween(LocalDate start, LocalDate end){
        return userMedicineRepository.findByExpiryDateBetween(start,end);
    }
}
