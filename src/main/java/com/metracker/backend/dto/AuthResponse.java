package com.metracker.backend.dto;

public class AuthResponse {
    public String token;
    public String email;
    public String role;
    public Long userId;

    public AuthResponse(String token, String email, String role, Long userId) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }
}
