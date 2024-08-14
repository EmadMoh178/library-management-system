package com.example.library_management.service;

import com.example.library_management.exception.NotFoundException;
import com.example.library_management.model.Patron;
import com.example.library_management.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    private Patron patron1;
    private Patron patron2;

    @BeforeEach
    public void setUp() {
        patron1 = new Patron(1L, "Patron 1", "patron1@example.com", "1234567890", LocalDate.now(), null);
        patron2 = new Patron(2L, "Patron 2", "patron2@example.com", "0987654321", LocalDate.now(), null);
    }

    @Test
    public void testGetAllPatrons() {
        when(patronRepository.findAll()).thenReturn(Arrays.asList(patron1, patron2));

        List<Patron> patrons = patronService.getAllPatrons();

        assertEquals(2, patrons.size());
        assertEquals("Patron 1", patrons.get(0).getName());
        assertEquals("Patron 2", patrons.get(1).getName());
    }

    @Test
    public void testGetPatronById() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron1));

        Patron patron = patronService.getPatronById(1L);

        assertNotNull(patron);
        assertEquals("Patron 1", patron.getName());
    }

    @Test
    public void testGetPatronByIdNotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            patronService.getPatronById(1L);
        });
    }

    @Test
    public void testAddPatron() {
        when(patronRepository.save(any(Patron.class))).thenReturn(patron1);

        String message = patronService.addPatron(patron1);

        assertEquals("Patron added successfully", message);
    }

    @Test
    public void testUpdatePatron() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron1));
        when(patronRepository.save(any(Patron.class))).thenReturn(patron1);

        String message = patronService.updatePatron(1L, patron1);

        assertEquals("Patron updated successfully", message);
    }

    @Test
    public void testUpdatePatronNotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            patronService.updatePatron(1L, patron1);
        });
    }

    @Test
    public void testDeletePatron() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron1));

        String message = patronService.deletePatron(1L);

        assertEquals("Patron deleted successfully", message);
        verify(patronRepository, times(1)).delete(patron1);
    }

    @Test
    public void testDeletePatronNotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            patronService.deletePatron(1L);
        });
    }
}