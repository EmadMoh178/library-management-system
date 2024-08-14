package com.example.library_management.service;

import com.example.library_management.model.Book;
import com.example.library_management.model.BorrowingRecord;
import com.example.library_management.model.Patron;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.BorrowingRecordRepository;
import com.example.library_management.repository.PatronRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BorrowingService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    public BorrowingService(BorrowingRecordRepository borrowingRecordRepository,
                            BookRepository bookRepository,
                            PatronRepository patronRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    @Transactional
    public String borrowBook(Long bookId, Long patronId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<Patron> patronOpt = patronRepository.findById(patronId);

        if (bookOpt.isEmpty()) {
            return "Book with ID " + bookId + " not found";
        }
        if (patronOpt.isEmpty()) {
            return "Patron with ID " + patronId + " not found";
        }

        Book book = bookOpt.get();
        Patron patron = patronOpt.get();

        Optional<BorrowingRecord> existingRecord = borrowingRecordRepository.findByBookIdAndReturnDateIsNull(bookId);
        if (existingRecord.isPresent()) {
            return "Book with ID " + bookId + " is already borrowed";
        }

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowDate(LocalDate.now());

        borrowingRecordRepository.save(record);
        return "Book with ID " + bookId + " borrowed successfully by Patron with ID " + patronId;
    }

    @Transactional
    public String returnBook(Long bookId, Long patronId) {
        Optional<BorrowingRecord> recordOpt = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId);
        if (!recordOpt.isPresent()) {
            return "Borrowing record not found for Book with ID " + bookId + " and Patron with ID " + patronId + ", or book already returned";
        }

        BorrowingRecord record = recordOpt.get();
        record.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(record);
        return "Book with ID " + bookId + " returned successfully by Patron with ID " + patronId;
    }
}