package com.example.library_management.controller;

import com.example.library_management.exception.NotFoundException;
import com.example.library_management.model.Patron;
import com.example.library_management.service.PatronService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
    public ResponseEntity<?> getPatronById(@PathVariable Long id) {
        try {
            Patron patron = patronService.getPatronById(id);
            return ResponseEntity.ok(patron);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> addPatron(@Valid @RequestBody Patron patron) {
        String message = patronService.addPatron(patron);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron updatedPatron) {
        try {
            String message = patronService.updatePatron(id, updatedPatron);
            return ResponseEntity.ok(message);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatron(@PathVariable Long id) {
        try {
            String message = patronService.deletePatron(id);
            return ResponseEntity.ok(message);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}