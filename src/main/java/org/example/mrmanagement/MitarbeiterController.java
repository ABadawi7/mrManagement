package org.example.mrmanagement;

// Damit reagieren wir auf POST-Anfragen.
import org.springframework.web.bind.annotation.PostMapping;

// Liest die JSON-Daten aus der Anfrage.
import org.springframework.web.bind.annotation.RequestBody;

// Kennzeichnet die Klasse als REST-Controller.
import org.springframework.web.bind.annotation.RestController;

@RestController
// Spring erkennt diese Klasse als Controller.
public class MitarbeiterController {

    // Zugriff auf die Datenbankklasse.
    private final MitarbeiterDao mitarbeiterDao;

    // Spring übergibt MitarbeiterDao automatisch.
    public MitarbeiterController(MitarbeiterDao mitarbeiterDao) {
        this.mitarbeiterDao = mitarbeiterDao;
    }

    @PostMapping("/api/mitarbeiter")
    // Diese Methode wird bei POST /api/mitarbeiter ausgeführt.
    public String mitarbeiterAnlegen(
            @RequestBody Mitarbeiter mitarbeiter
            // JSON-Daten werden automatisch in ein Mitarbeiter-Objekt umgewandelt.
    ) {

        // Speichert den Mitarbeiter in der Datenbank.
        int anzahl = mitarbeiterDao.speichern(mitarbeiter);

        // Prüft, ob genau ein Datensatz gespeichert wurde.
        if (anzahl == 1) {
            return "Mitarbeiter wurde gespeichert\n";
        }

        return "Mitarbeiter konnte nicht gespeichert werden";
    }
}