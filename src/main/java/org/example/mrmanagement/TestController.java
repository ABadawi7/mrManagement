package org.example.mrmanagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Markiert die Klasse als Controller für HTTP-Anfragen.
public class TestController {
    @GetMapping("/api/test")
    public String test() {
        return "Mitarbeiterverwaltung läuft";
    }
}
