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

@Configuration
// Spring lädt diese Sicherheitskonfiguration beim Start.
public class SecurityConfig {

    @Bean
    // Spring verwendet diese Methode als Sicherheitskonfiguration.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // Diese Testadresse darf ohne Anmeldung geöffnet werden.
                        .requestMatchers("/api/test").permitAll()

                        // Alle anderen Seiten benötigen weiterhin eine Anmeldung.
                        .anyRequest().authenticated()
                )

                // Die automatische Login-Seite bleibt vorerst aktiv.
                .formLogin(form -> form.permitAll());

        // Erstellt die fertige Sicherheitskonfiguration.
        return http.build();
    }
}