package com.metracker.backend.service;

import com.metracker.backend.model.Alert;
import com.metracker.backend.model.AlertType;
import com.metracker.backend.model.UserMedicine;
import com.metracker.backend.repository.AlertRepository;
import com.metracker.backend.repository.UserMedicineRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AlertService {
    private final UserMedicineRepository userMedicineRepository;
    private final AlertRepository alertRepository;
    private final JavaMailSender javaMailSender;

    @Value("${app.alert.days-before-expriy:14}")
    private int daysBefore;
    public AlertService(UserMedicineRepository userMedicineRepository, AlertRepository alertRepository, JavaMailSender javaMailSender) {
        this.userMedicineRepository = userMedicineRepository;
        this.alertRepository = alertRepository;
        this.javaMailSender = javaMailSender;
    }

    @Scheduled(cron = "0 0 8 * * *") // Every day at 8 AM
    public void checkAndSendExpiryAlerts() {
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(daysBefore);
        List<UserMedicine> soon = userMedicineRepository.findByExpiryDateBetween(today,threshold);

        for (UserMedicine um : soon) {
            try {
                // check if an alert was already sent recently
                LocalDateTime from = LocalDateTime.now().minusDays(7);
                boolean already = alertRepository.findByUserIdAndMedicineAndTypeAndAlertDateBetween(
                        um.getUser().getId(),
                        um.getMedicine(),
                        AlertType.EXPIRY,
                        java.sql.Timestamp.valueOf(from),
                        java.sql.Timestamp.valueOf(LocalDateTime.now())
                ).isPresent();
                if (already) continue;

                // send email
                String to = um.getUser().getEmail();
                String subject = "Medicine expiry reminder: " + um.getMedicine().getName();
                String body = String.format("Dear %s,\n\nYour medicine '%s' (expiry %s) will expire on %s. Please consume or donate if eligible.\n\n- MedTrack",
                        um.getUser().getName(),
                        um.getMedicine().getName(),
                        um.getExpiryDate().toString(),
                        um.getExpiryDate().toString());

                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(to);
                msg.setSubject(subject);
                msg.setText(body);
                javaMailSender.send(msg);

                // save Alert record
                Alert alert = new Alert();
                alert.setUser(um.getUser());
                alert.setMedicine(um.getMedicine());
                alert.setType("expiry");
                alert.setAlertDate(LocalDateTime.now());
                alertRepository.save(alert);

            } catch (Exception ex) {
                // log error
                ex.printStackTrace();
            }
        }

    }
}
