package com.example.library_management.controller;

import com.example.library_management.model.Book;
import com.example.library_management.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(book.toString()))  // Convert the book object to string or JSON
                .orElse(ResponseEntity.status(404).body("Book with ID " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        String message = bookService.addBook(book);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        String message = bookService.updateBook(id, updatedBook);
        return ResponseEntity.status(message.equals("Book with ID " + id + " not found") ? 404 : 200).body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        String message = bookService.deleteBook(id);
        return ResponseEntity.status(message.equals("Book with ID " + id + " not found") ? 404 : 200).body(message);
    }
}
