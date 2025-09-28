package com.metracker.backend.service;

import com.metracker.backend.model.PharmacyStock;
import com.metracker.backend.model.User;
import com.metracker.backend.repository.PharmacyStockRepository;
import com.metracker.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SearchService {
    private final UserRepository userRepository;
    private final PharmacyStockRepository pharmacyStockRepository;

    public SearchService(PharmacyStockRepository pharmacyStockRepository,UserRepository userRepository) {
        this.userRepository = userRepository;
        this.pharmacyStockRepository = pharmacyStockRepository;
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R=6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R*c;
    }

    public List<PharmacyStock> findNearBy(Long userId, String medicineName, int limit) {
        User user = userRepository.findById(userId).orElse(null);
        double lat = user.getLatitude() == null ? 0.0 : user.getLatitude();
        double lon = user.getLongitude() == null ? 0.0 : user.getLongitude();

        List<PharmacyStock> all = pharmacyStockRepository.findAll();
        List<PharmacyStock> filtered = all.stream()
                .filter(s -> s.getMedicine().getName().equalsIgnoreCase(medicineName) && s.getQuantity() > 0)
                .collect(Collectors.toList());
        return filtered.stream()
                .sorted(Comparator.comparingDouble(s -> distanceKm(lat, lon,
                        s.getPharmacy().getLatitude() == null ? 0.0 : s.getPharmacy().getLatitude(),
                        s.getPharmacy().getLongitude() == null ? 0.0 : s.getPharmacy().getLongitude())))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
