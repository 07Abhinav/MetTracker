package com.metracker.backend.repository;

import com.metracker.backend.model.PharmacyStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacyStockRepository extends JpaRepository<PharmacyStock,Long> {
    List<PharmacyStock> findByMedicineId(Long id);
    List<PharmacyStock> findByMedicineNameIgnoreCase(String name);
}
