package org.example.mrmanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
// Marks this class as a REST controller.
public class MitarbeiterController {

    // Provides access to employee database operations.
    private final MitarbeiterDao mitarbeiterDao;

    // Spring injects MitarbeiterDao automatically.
    public MitarbeiterController(MitarbeiterDao mitarbeiterDao) {
        this.mitarbeiterDao = mitarbeiterDao;
    }

    @PostMapping("/api/mitarbeiter")
    // Handles POST requests for creating a new employee.
    public String mitarbeiterAnlegen(
            @RequestBody Mitarbeiter mitarbeiter
    ) {

        // Saves the received employee in the database.
        int anzahl = mitarbeiterDao.speichern(mitarbeiter);

        // Returns a success message when exactly one row was inserted.
        if (anzahl == 1) {
            return "Mitarbeiter wurde gespeichert";
        }

        // Returns an error message when no employee was inserted.
        return "Mitarbeiter konnte nicht gespeichert werden";
    }

    @GetMapping("/api/mitarbeiter")
    // Handles GET requests for loading all employees.
    public List<Mitarbeiter> alleMitarbeiterLaden() {

        // Loads and returns all employees from the database.
        return mitarbeiterDao.alleLaden();
    }
}