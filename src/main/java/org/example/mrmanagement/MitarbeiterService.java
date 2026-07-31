package org.example.mrmanagement;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Service
// Contains the business logic for employee management.
public class MitarbeiterService {

    // Generates secure random employee numbers and passwords.
    private final SecureRandom secureRandom =
            new SecureRandom();

    // Provides access to employee database operations.
    private final MitarbeiterDao mitarbeiterDao;

    // Creates secure password hashes.
    private final PasswordEncoder passwordEncoder;

    // Spring injects the required dependencies automatically.
    public MitarbeiterService(
            MitarbeiterDao mitarbeiterDao,
            PasswordEncoder passwordEncoder) {

        this.mitarbeiterDao = mitarbeiterDao;
        this.passwordEncoder = passwordEncoder;
    }

    // Creates a new employee and automatically generates a one-time password.
    public String mitarbeiterAnlegen(Mitarbeiter mitarbeiter) {

        // Generates a unique employee number.
        String employeeNumber =
                generateUniqueEmployeeNumber();

        // Assigns the employee number.
        mitarbeiter.setMitarbeiterNr(employeeNumber);

        // Saves the employee first.
        int insertedRows =
                mitarbeiterDao.speichern(mitarbeiter);

        if (insertedRows != 1) {
            throw new IllegalStateException(
                    "Mitarbeiter konnte nicht gespeichert werden."
            );
        }

        // Generates the one-time password.
        String oneTimePassword =
                generateOneTimePassword();

        // Creates a secure password hash.
        String passwordHash =
                passwordEncoder.encode(oneTimePassword);

        // Sets the password validity to 15 minutes.
        LocalDateTime validUntil =
                LocalDateTime.now().plusMinutes(15);

        // Stores only the password hash.
        int updatedRows =
                mitarbeiterDao.saveOneTimePassword(
                        employeeNumber,
                        passwordHash,
                        validUntil
                );

        if (updatedRows != 1) {
            throw new IllegalStateException(
                    "Einmalpasswort konnte nicht gespeichert werden."
            );
        }

        // Returns the plain-text password only once.
        return oneTimePassword;
    }

    // Deletes an employee permanently.
    public int deleteByEmployeeNumber(
            String employeeNumber) {

        return mitarbeiterDao
                .deleteByEmployeeNumber(employeeNumber);
    }

    // Deactivates an employee without deleting the record.
    public int deactivateByEmployeeNumber(
            String employeeNumber) {

        return mitarbeiterDao
                .deactivateByEmployeeNumber(employeeNumber);
    }

    // Activates an employee.
    public int activateByEmployeeNumber(
            String employeeNumber) {

        return mitarbeiterDao
                .activateByEmployeeNumber(employeeNumber);
    }

    // Generates a unique six-digit employee number.
    private String generateUniqueEmployeeNumber() {

        String employeeNumber;

        do {

            // Generates a number between 100000 and 999999.
            employeeNumber = String.valueOf(
                    secureRandom.nextInt(900000) + 100000
            );

            // Repeats when the generated number already exists.
        } while (
                mitarbeiterDao
                        .existsByEmployeeNumber(employeeNumber)
        );

        return employeeNumber;
    }

    // Generates a secure one-time password.
    private String generateOneTimePassword() {

        // Characters allowed in the one-time password.
        String characters = "ABCDEFGHJKLMNPQRSTUVWXYZ" + "abcdefghijkmnopqrstuvwxyz" + "23456789";
        StringBuilder password = new StringBuilder();

        // Generates a twelve-character password.
        for (int i = 0; i < 10; i++) {
            int index = secureRandom.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }
    // Creates and stores a new one-time password.
    public String createOneTimePassword(String employeeNumber) {

        // Generates the one-time password in plain text.
        String oneTimePassword =
                generateOneTimePassword();

        // Creates a secure hash for database storage.
        String passwordHash =
                passwordEncoder.encode(oneTimePassword);

        // Sets the expiration time to 15 minutes.
        LocalDateTime validUntil =
                LocalDateTime.now().plusMinutes(15);

        // Stores only the hash and expiration time.
        int updatedRows = mitarbeiterDao.saveOneTimePassword(
                        employeeNumber,
                        passwordHash,
                        validUntil
                );

        // Stops when no matching employee was found.
        if (updatedRows == 0) {
            throw new IllegalArgumentException(
                    "Mitarbeiter wurde nicht gefunden."
            );
        }

        // Returns the plain-text password only once.
        return oneTimePassword;
    }
    // Checks whether the one-time password is valid.
    public boolean validateOneTimePassword(
            String employeeNumber,
            String oneTimePassword) {

        Map<String, Object> passwordData =
                mitarbeiterDao.loadOneTimePasswordData(
                        employeeNumber
                );

        // Stops when the employee does not exist.
        if (passwordData == null) {
            return false;
        }

        boolean active =
                Boolean.TRUE.equals(passwordData.get("aktiv"));

        boolean used =
                Boolean.TRUE.equals(
                        passwordData.get("einmalpasswort_verwendet")
                );

        String passwordHash =
                (String) passwordData.get(
                        "einmalpasswort_hash"
                );

        LocalDateTime validUntil =
                ((Timestamp) passwordData.get(
                        "einmalpasswort_gueltig_bis"
                )).toLocalDateTime();

        // Checks all required conditions.
        return active
                && !used
                && passwordHash != null
                && validUntil != null
                && LocalDateTime.now().isBefore(validUntil)
                && passwordEncoder.matches(
                oneTimePassword,
                passwordHash
        );
    }
}