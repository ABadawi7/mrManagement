package org.example.mrmanagement;

import java.time.LocalDate;
// LocalDate wird für das Eintrittsdatum verwendet.

public class Mitarbeiter {

    // Eindeutige ID aus der Datenbank.
    private Long id;

    // Interne Mitarbeiternummer, zum Beispiel 1004.
    private String mitarbeiterNr;

    // Vorname des Mitarbeiters.
    private String vorname;

    // Nachname des Mitarbeiters.
    private String nachname;

    // Filiale, zum Beispiel DKA106.
    private String filialeCode;

    // Rolle, zum Beispiel KASSIERER oder MANAGER.
    private String rolle;

    // Gibt an, ob der Mitarbeiter aktiv ist.
    private boolean aktiv;

    // Datum, an dem der Mitarbeiter angefangen hat.
    private LocalDate eintrittsdatum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMitarbeiterNr() {
        return mitarbeiterNr;
    }

    public void setMitarbeiterNr(String mitarbeiterNr) {
        this.mitarbeiterNr = mitarbeiterNr;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getFilialeCode() {
        return filialeCode;
    }

    public void setFilialeCode(String filialeCode) {
        this.filialeCode = filialeCode;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public LocalDate getEintrittsdatum() {
        return eintrittsdatum;
    }

    public void setEintrittsdatum(LocalDate eintrittsdatum) {
        this.eintrittsdatum = eintrittsdatum;
    }
}