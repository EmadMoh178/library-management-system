package com.example.library_management.service;

import com.example.library_management.model.Book;
import com.example.library_management.model.BorrowingRecord;
import com.example.library_management.model.Patron;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.BorrowingRecordRepository;
import com.example.library_management.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingService borrowingService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    public void setUp() {
        book = new Book(1L, "Book 1", "Author 1", 2020, "1234567890123", null);
        patron = new Patron(1L, "Patron 1", "patron1@example.com", "1234567890", LocalDate.now(), null);
        borrowingRecord = new BorrowingRecord(1L, LocalDate.now(), null, book, patron);
    }

    @Test
    public void testBorrowBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(1L)).thenReturn(Optional.empty());
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        String message = borrowingService.borrowBook(1L, 1L);

        assertEquals("Book with ID 1 borrowed successfully by Patron with ID 1", message);
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    public void testBorrowBookAlreadyBorrowed() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(1L)).thenReturn(Optional.of(borrowingRecord));

        String message = borrowingService.borrowBook(1L, 1L);

        assertEquals("Book with ID 1 is already borrowed", message);
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    public void testBorrowBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        String message = borrowingService.borrowBook(1L, 1L);

        assertEquals("Book with ID 1 not found", message);
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    public void testBorrowPatronNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        String message = borrowingService.borrowBook(1L, 1L);

        assertEquals("Patron with ID 1 not found", message);
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    public void testReturnBook() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Optional.of(borrowingRecord));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        String message = borrowingService.returnBook(1L, 1L);

        assertEquals("Book with ID 1 returned successfully by Patron with ID 1", message);
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    public void testReturnBookAlreadyReturned() {
        borrowingRecord.setReturnDate(LocalDate.now());

        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Optional.empty());

        String result = borrowingService.returnBook(1L, 1L);

        assertEquals("Borrowing record not found for Book with ID 1 and Patron with ID 1, or book already returned", result);
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    public void testReturnBookNotFound() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Optional.empty());

        String message = borrowingService.returnBook(1L, 1L);

        assertEquals("Borrowing record not found for Book with ID 1 and Patron with ID 1, or book already returned", message);
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }
}