package com.example.library_management.controller;

import com.example.library_management.service.BorrowingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowingController.class)
public class BorrowingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingService borrowingService;

    @Test
    public void testBorrowBook() throws Exception {
        when(borrowingService.borrowBook(1L, 1L)).thenReturn("Book with ID 1 borrowed successfully by Patron with ID 1");

        mockMvc.perform(post("/api/borrow/1/patron/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book with ID 1 borrowed successfully by Patron with ID 1"));
    }

    @Test
    public void testBorrowBookNotFound() throws Exception {
        when(borrowingService.borrowBook(1L, 1L)).thenReturn("Book with ID 1 not found");

        mockMvc.perform(post("/api/borrow/1/patron/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book with ID 1 not found"));
    }

    @Test
    public void testBorrowBookAlreadyBorrowed() throws Exception {
        when(borrowingService.borrowBook(1L, 1L)).thenReturn("Book with ID 1 is already borrowed");

        mockMvc.perform(post("/api/borrow/1/patron/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book with ID 1 is already borrowed"));
    }

    @Test
    public void testReturnBook() throws Exception {
        when(borrowingService.returnBook(1L, 1L)).thenReturn("Book with ID 1 returned successfully by Patron with ID 1");

        mockMvc.perform(put("/api/return/1/patron/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book with ID 1 returned successfully by Patron with ID 1"));
    }

    @Test
    public void testReturnBookNotFound() throws Exception {
        when(borrowingService.returnBook(1L, 1L)).thenReturn("Borrowing record not found for Book with ID 1 and Patron with ID 1, or book already returned");

        mockMvc.perform(put("/api/return/1/patron/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Borrowing record not found for Book with ID 1 and Patron with ID 1, or book already returned"));
    }
}