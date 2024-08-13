package com.example.library_management.controller;

import com.example.library_management.model.Patron;
import com.example.library_management.service.PatronService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public List<Patron> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPatronById(@PathVariable Long id) {
        return patronService.getPatronById(id)
                .map(patron -> ResponseEntity.ok(patron.toString()))  // Convert the patron object to string or JSON
                .orElse(ResponseEntity.status(404).body("Patron with ID " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<String> addPatron(@RequestBody Patron patron) {
        String message = patronService.addPatron(patron);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePatron(@PathVariable Long id, @RequestBody Patron updatedPatron) {
        String message = patronService.updatePatron(id, updatedPatron);
        return ResponseEntity.status(message.equals("Patron with ID " + id + " not found") ? 404 : 200).body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatron(@PathVariable Long id) {
        String message = patronService.deletePatron(id);
        return ResponseEntity.status(message.equals("Patron with ID " + id + " not found") ? 404 : 200).body(message);
    }
}