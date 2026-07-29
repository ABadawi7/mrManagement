package org.example.mrmanagement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
// Contains the business logic for employee management.
public class MitarbeiterService {
    // Generates secure random employee numbers.
    private final SecureRandom secureRandom = new SecureRandom();

    // Provides access to employee database operations.
    private final MitarbeiterDao mitarbeiterDao;

    // Spring injects MitarbeiterDao automatically.
    public MitarbeiterService(MitarbeiterDao mitarbeiterDao) {
        this.mitarbeiterDao = mitarbeiterDao;
    }

    public int  mitarbeiterAnlegen(Mitarbeiter mitarbeiter) {
        // Generates a unique six-digit employee number.
        String employeeNumber = generateUniqueEmployeeNumber();
        // Assigns the generated number to the employee.
        mitarbeiter.setMitarbeiterNr(employeeNumber);
        // Saves the employee in the database.
        return mitarbeiterDao.speichern(mitarbeiter);
    }
    // Deletes an employee by employee number.
    public int deleteByEmployeeNumber(String employeeNumber) {
        return mitarbeiterDao.deleteByEmployeeNumber(employeeNumber);
    }
    private String generateUniqueEmployeeNumber() {
        String employeeNumber;

        do{
            // Generates a number between 100000 and 999999.
            employeeNumber = String.valueOf(secureRandom.nextInt(900000) + 100000);

            // Repeats when the generated number already exists.
        }while(mitarbeiterDao.existsByEmployeeNumber(employeeNumber));

        return employeeNumber;
    }


}
