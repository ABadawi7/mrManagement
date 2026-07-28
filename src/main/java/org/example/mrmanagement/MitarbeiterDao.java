package org.example.mrmanagement;

// JdbcTemplate führt SQL-Befehle aus.
import org.springframework.jdbc.core.JdbcTemplate;

// Kennzeichnet die Klasse als Datenbankklasse.
import org.springframework.stereotype.Repository;

@Repository
// Spring erstellt automatisch ein Objekt dieser Klasse.
public class MitarbeiterDao {

    // Verbindung zur MySQL-Datenbank.
    private final JdbcTemplate jdbcTemplate;

    // Spring übergibt JdbcTemplate automatisch.
    public MitarbeiterDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Speichert einen neuen Mitarbeiter in der Datenbank.
    public int speichern(Mitarbeiter mitarbeiter) {

        // SQL-Befehl zum Einfügen eines Mitarbeiters.
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

        // Führt den SQL-Befehl aus.
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
}