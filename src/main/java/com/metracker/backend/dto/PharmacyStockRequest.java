package com.metracker.backend.dto;

import java.time.LocalDate;

public class PharmacyStockRequest {
    public Long medicineId;
    public String medicineName;
    public Integer quantity;
    public LocalDate expiryDate;
}
