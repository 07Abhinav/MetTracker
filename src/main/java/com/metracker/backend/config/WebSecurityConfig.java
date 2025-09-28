//// src/main/java/com/metracker/backend/config/WebSecurityConfig.java
//package com.metracker.backend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class WebSecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Use lambda for newer Spring Security
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/medicines/me").permitAll()
//                        .anyRequest().authenticated()
//                );
//        return http.build();
//    }
//}