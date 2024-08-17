package com.example.library_management.service;

import com.example.library_management.exception.NotFoundException;
import com.example.library_management.model.Book;
import com.example.library_management.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "'allBooks'")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "#id")
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with ID " + id + " not found"));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "books", key = "'allBooks'"),
            @CacheEvict(value = "books", allEntries = true)
    })
    public String addBook(Book book) {
        bookRepository.save(book);
        return "Book added successfully";
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "books", key = "'allBooks'"),
            @CacheEvict(value = "books", key = "#id")
    })
    public String updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPublicationYear(updatedBook.getPublicationYear());
            book.setIsbn(updatedBook.getIsbn());
            bookRepository.save(book);
            return "Book updated successfully";
        }).orElseThrow(() -> new NotFoundException("Book with ID " + id + " not found"));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "books", key = "'allBooks'"),
            @CacheEvict(value = "books", key = "#id")
    })
    public String deleteBook(Long id) {
        return bookRepository.findById(id).map(book -> {
            bookRepository.delete(book);
            return "Book deleted successfully";
        }).orElseThrow(() -> new NotFoundException("Book with ID " + id + " not found"));
    }
}