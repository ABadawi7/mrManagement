package org.example.mrmanagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class Mitarbeiter {

    // Unique database ID.
    private Long id;

    // Internal six-digit employee number.
    private String mitarbeiterNr;

    // The first name must not be empty.
    @NotBlank
    private String vorname;

    // The last name must not be empty.
    @NotBlank
    private String nachname;

    // The branch code must not be empty.
    @NotBlank
    private String filialeCode;

    // The employee role must not be empty.
    @NotBlank
    private String rolle;

    // Indicates whether the employee is active.
    private boolean aktiv;

    // The employment start date must not be in the future.
    @PastOrPresent
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