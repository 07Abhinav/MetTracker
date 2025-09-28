package com.metracker.backend.repository;

import com.metracker.backend.model.Alert;
import com.metracker.backend.model.AlertType;
import com.metracker.backend.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert,Long> {
    Optional<Alert> findByUserIdAndMedicineAndTypeAndAlertDateBetween(
            Long userId,
            Medicine medicine,
            AlertType type,
            Date startDate,
            Date endDate
    );
}