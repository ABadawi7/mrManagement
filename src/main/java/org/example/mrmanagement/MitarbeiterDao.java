package org.example.mrmanagement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Marks this class as a database repository.
public class MitarbeiterDao {

    // Provides access to the MySQL database.
    private final JdbcTemplate jdbcTemplate;

    // Spring injects JdbcTemplate automatically.
    public MitarbeiterDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Saves a new employee in the database.
    public int speichern(Mitarbeiter mitarbeiter) {

        // SQL statement for inserting a new employee.
        String sql = """
                INSERT INTO mitarbeiter
                (
                    mitarbeiter_nr,
                    vorname,
                    nachname,
                    filiale_code,
                    rolle,
                    aktiv,
                    eintrittsdatum
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        // Executes the INSERT statement with the employee data.
        return jdbcTemplate.update(
                sql,
                mitarbeiter.getMitarbeiterNr(),
                mitarbeiter.getVorname(),
                mitarbeiter.getNachname(),
                mitarbeiter.getFilialeCode(),
                mitarbeiter.getRolle(),
                mitarbeiter.isAktiv(),
                mitarbeiter.getEintrittsdatum()
        );
    }

    // Loads all employees from the database.
    public List<Mitarbeiter> alleLaden() {

        // SQL query for loading all employees.
        String sql = """
                SELECT
                    id,
                    mitarbeiter_nr,
                    vorname,
                    nachname,
                    filiale_code,
                    rolle,
                    aktiv,
                    eintrittsdatum
                FROM mitarbeiter
                ORDER BY id
                """;

        // Executes the query and converts each row into a Mitarbeiter object.
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {

            // Creates a new employee object.
            Mitarbeiter mitarbeiter = new Mitarbeiter();

            // Copies the database values into the employee object.
            mitarbeiter.setId(resultSet.getLong("id"));
            mitarbeiter.setMitarbeiterNr(
                    resultSet.getString("mitarbeiter_nr")
            );
            mitarbeiter.setVorname(
                    resultSet.getString("vorname")
            );
            mitarbeiter.setNachname(
                    resultSet.getString("nachname")
            );
            mitarbeiter.setFilialeCode(
                    resultSet.getString("filiale_code")
            );
            mitarbeiter.setRolle(
                    resultSet.getString("rolle")
            );
            mitarbeiter.setAktiv(
                    resultSet.getBoolean("aktiv")
            );

            // Reads the employment start date only when it is not null.
            if (resultSet.getDate("eintrittsdatum") != null) {
                mitarbeiter.setEintrittsdatum(
                        resultSet
                                .getDate("eintrittsdatum")
                                .toLocalDate()
                );
            }

            // Returns the completed employee object.
            return mitarbeiter;
        });
    }
    public boolean existsByEmployeeNumber(String employeeNumber) {
        // Counts matching employee numbers in the database.
        String sql = """
                SELECT COUNT(*) FROM mitarbeiter WHERE mitarbeiter_nr = ?;
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, employeeNumber);
        //Returns true when the number already exists.
        return count != null && count > 0;
    }

    // Updates an existing employee in the database.
    public int aktualisierung(long id, Mitarbeiter mitarbeiter) {

        // SQL statement for updating employee data.
        String sql = """
                UPDATE mitarbeiter
                SET
                    vorname = ?,
                    nachname = ?,
                    filiale_code = ?,
                    rolle = ?,
                    aktiv = ?,
                    eintrittsdatum = ?
                    WHERE id = ?""";

        return jdbcTemplate.update(
                sql,mitarbeiter.getVorname(),mitarbeiter.getNachname(),
                mitarbeiter.getFilialeCode(),
                mitarbeiter.getRolle(),mitarbeiter.isAktiv(),
                mitarbeiter.getEintrittsdatum(),id
        );
    }
    public int deleteByEmployeeNumber(String employeeNumber) {

        String sql = """
                DELETE FROM mitarbeiter
                WHERE mitarbeiter_nr = ?;
        """;

        return jdbcTemplate.update(sql, employeeNumber);
    }
    // Deactivates an employee by employee number.
    public int deactivateByEmployeeNumber(String employeeNumber) {

        // SQL statement for setting the employee status to inactive.
        String sql = """
            UPDATE mitarbeiter
            SET aktiv = false
            WHERE mitarbeiter_nr = ?
            """;

        // Executes the UPDATE statement.
        return jdbcTemplate.update(sql, employeeNumber);
    }
}