package com.example.library_management.controller;

import com.example.library_management.service.BorrowingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        String message = borrowingService.borrowBook(bookId, patronId);
        if (message.contains("not found") || message.contains("already borrowed")) {
            return ResponseEntity.status(400).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        String message = borrowingService.returnBook(bookId, patronId);
        if (message.contains("not found") || message.contains("already returned")) {
            return ResponseEntity.status(400).body(message);
        }
        return ResponseEntity.ok(message);
    }
}
