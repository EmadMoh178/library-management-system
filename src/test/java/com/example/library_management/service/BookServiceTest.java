package com.example.library_management.service;

import com.example.library_management.exception.NotFoundException;
import com.example.library_management.model.Book;
import com.example.library_management.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        book1 = new Book(1L, "Book 1", "Author 1", 2020, "1234567890123", null);
        book2 = new Book(2L, "Book 2", "Author 2", 2021, "9876543210987", null);
    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        assertEquals("Book 2", books.get(1).getTitle());
    }

    @Test
    public void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Book book = bookService.getBookById(1L);

        assertNotNull(book);
        assertEquals("Book 1", book.getTitle());
    }

    @Test
    public void testGetBookByIdNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            bookService.getBookById(1L);
        });
    }

    @Test
    public void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        String message = bookService.addBook(book1);

        assertEquals("Book added successfully", message);
    }

    @Test
    public void testUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        String message = bookService.updateBook(1L, book1);

        assertEquals("Book updated successfully", message);
    }

    @Test
    public void testUpdateBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            bookService.updateBook(1L, book1);
        });
    }

    @Test
    public void testDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        String message = bookService.deleteBook(1L);

        assertEquals("Book deleted successfully", message);
        verify(bookRepository, times(1)).delete(book1);
    }

    @Test
    public void testDeleteBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });
    }
}