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
    public ResponseEntity<String> mitarbeiterAnlegen(
            @Valid @RequestBody Mitarbeiter mitarbeiter) {

        try {

            String oneTimePassword =
                    mitarbeiterService.mitarbeiterAnlegen(mitarbeiter);

            return ResponseEntity.ok("Mitarbeiter wurde gespeichert." +
                    "\nDie Mitarbeiternummer lautet: " + mitarbeiter.getMitarbeiterNr()+
                    "  Einmalpasswort: "  + oneTimePassword
            );

        } catch (IllegalStateException exception) {

            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
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
    // Deactivates an employee by employee number.
    @PutMapping("/nummer/{employeeNumber}/deaktivieren")
    public ResponseEntity<String> deactivateEmployee(
            @PathVariable String employeeNumber) {

        int updatedRows =
                mitarbeiterService.deactivateByEmployeeNumber(employeeNumber);

        if (updatedRows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Mitarbeiter wurde deaktiviert."
        );
    }
    // Activates an employee by employee number.
    @PutMapping("/nummer/{employeeNumber}/aktivieren")
    public ResponseEntity<String> activateEmployee(
            @PathVariable String employeeNumber) {

        int updatedRows =
                mitarbeiterService.activateByEmployeeNumber(employeeNumber);

        if (updatedRows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Mitarbeiter wurde aktiviert."
        );
    }
    // Creates a new one-time password for an employee.
    @PostMapping("/nummer/{employeeNumber}/einmalpasswort")
    public ResponseEntity<String> createOneTimePassword(
            @PathVariable String employeeNumber) {

        try {

            String oneTimePassword =
                    mitarbeiterService.createOneTimePassword(
                            employeeNumber
                    );

            return ResponseEntity.ok(oneTimePassword);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity.notFound().build();
        }
    }


}