package com.metracker.backend.repository;

import com.metracker.backend.model.UserMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserMedicineRepository extends JpaRepository<UserMedicine,Long> {
    List<UserMedicine> findByUserId(Long id);
    List<UserMedicine> findByExpiryDateBetween(LocalDate start, LocalDate end);
    List<UserMedicine> findByExpiryDateBefore(LocalDate date);
}
