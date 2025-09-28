package com.metracker.backend.repository;

import com.metracker.backend.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine,Long> {
    Optional<Medicine> findByNameIgnoreCase(String name);
}
