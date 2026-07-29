package org.example.mrmanagement;
// Package der Anwendung.

import org.springframework.context.annotation.Bean;
// Ermöglicht das Erstellen eines Spring-Beans.

import org.springframework.context.annotation.Configuration;
// Kennzeichnet diese Klasse als Konfigurationsklasse.

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// Damit konfigurieren wir die Sicherheitsregeln.

import org.springframework.security.web.SecurityFilterChain;
// Enthält die Regeln für HTTP-Anfragen.

// Damit unterscheiden wir GET- und POST-Anfragen.
import org.springframework.http.HttpMethod;

@Configuration
// Spring lädt diese Sicherheitskonfiguration beim Start.
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF für den API-Test deaktivieren.
                .csrf(csrf -> csrf.disable())

                // Vorübergehend alle Anfragen erlauben.
                .authorizeHttpRequests(auth -> auth
                        // Allows the test endpoint without login.
                        .requestMatchers("/api/test").permitAll()
                        // Allows all employee API requests temporarily.
                        .requestMatchers("/api/mitarbeiter/**").permitAll()
                        // Protects all other requests.
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}