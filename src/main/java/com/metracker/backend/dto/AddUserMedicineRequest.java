// src/main/java/com/metracker/backend/dto/AddUserMedicineRequest.java
package com.metracker.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class AddUserMedicineRequest {
    @JsonProperty("name")
    private String medicineName;
    private String description;
    private String manufacturer;
    private String dosage;
    private LocalDate expiryDate;
}