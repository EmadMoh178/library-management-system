package com.example.library_management.controller;

import com.example.library_management.exception.NotFoundException;
import com.example.library_management.model.Book;
import com.example.library_management.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> addBook(@Valid @RequestBody Book book) {
        String message = bookService.addBook(book);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @Valid @RequestBody Book updatedBook) {
        try {
            String message = bookService.updateBook(id, updatedBook);
            return ResponseEntity.ok(message);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        try {
            String message = bookService.deleteBook(id);
            return ResponseEntity.ok(message);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}