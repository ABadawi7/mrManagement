package org.example.mrmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;


import java.util.List;

@RestController
// Marks this class as a REST controller.
@RequestMapping("/api/mitarbeiter")
public class MitarbeiterController {

    // Provides access to employee business logic.
    private final MitarbeiterService mitarbeiterService;

    // Provides access to employee database operations.
    private final MitarbeiterDao mitarbeiterDao;

    // Spring injects MitarbeiterDao automatically.
    public MitarbeiterController(MitarbeiterService mitarbeiterService,MitarbeiterDao mitarbeiterDao) {
        this.mitarbeiterDao = mitarbeiterDao;
        this.mitarbeiterService = mitarbeiterService;
    }
    @GetMapping
    // Handles GET requests for loading all employees.
    public List<Mitarbeiter> alleMitarbeiterLaden() {

        // Loads and returns all employees from the database.
        return mitarbeiterDao.alleLaden();
    }

    @PostMapping
    // Handles POST requests for creating a new employee.
    public String mitarbeiterAnlegen(@Valid @RequestBody Mitarbeiter mitarbeiter) {

        // Saves the received employee in the database.
        int anzahl = mitarbeiterService.mitarbeiterAnlegen(mitarbeiter);

        // Returns a success message when exactly one row was inserted.
        if (anzahl == 1) {
            return "Mitarbeiter wurde gespeichert";
        }

        // Returns an error message when no employee was inserted.
        return "Mitarbeiter konnte nicht gespeichert werden";
    }



    @PutMapping("/{id}")
    // Handels PUT requests for updating an existing employee.
    public String mitarbeiterAktualisieren(@PathVariable long id,@Valid @RequestBody Mitarbeiter mitarbeiter) {
        // Updates the employee with the given database ID.
        int count = mitarbeiterDao.aktualisierung(id, mitarbeiter);

        // Returns a success message when exactly one row was updated.
        if(count==1) {
            return "Mitarbeiter wurde aktualisiert";
        }

        //Returns an error message when no matching employee was found.
        return "Mitarbeiter konnte nicht gefunden werden";
    }
    // Deletes an employee by employee number.
    @DeleteMapping("/nummer/{employeeNumber}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable String employeeNumber) {

        int deletedRows =
                mitarbeiterService.deleteByEmployeeNumber(employeeNumber);

        if (deletedRows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Mitarbeiter wurde gelöscht.");
    }


}