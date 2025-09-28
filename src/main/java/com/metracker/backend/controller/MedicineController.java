package com.metracker.backend.controller;

import com.metracker.backend.dto.AddUserMedicineRequest;
import com.metracker.backend.model.User;
import com.metracker.backend.model.UserMedicine;
import com.metracker.backend.repository.UserRepository;
import com.metracker.backend.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {
    private final MedicineService medicineService;
    private final UserRepository userRepository;

    public MedicineController(MedicineService medicineService, UserRepository userRepository) {
        this.medicineService = medicineService;
        this.userRepository = userRepository;
    }

    @PostMapping("/me")
    public ResponseEntity<?> addByMedicine(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody AddUserMedicineRequest req) {

        String email = principal.getUsername(); // comes from JWT
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {  // ✅ fixed condition
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();
        UserMedicine added = medicineService.addUserMedicine(user.getId(), req);
        return ResponseEntity.ok(added);
    }


    @GetMapping("/me")
    public ResponseEntity<?> myMedicines(@AuthenticationPrincipal UserDetails principal){
        String email = principal.getUsername();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty()){
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();
        List<UserMedicine> meds = medicineService.listUserMedicines(user.getId());
        return ResponseEntity.ok(meds);
    }
}
