package com.example.library_management.controller;

import com.example.library_management.exception.NotFoundException;
import com.example.library_management.model.Patron;
import com.example.library_management.service.PatronService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatronController.class)
public class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    @Autowired
    private ObjectMapper objectMapper;

    private Patron patron1;
    private Patron patron2;

    @BeforeEach
    public void setUp() {
        patron1 = new Patron(1L, "Patron 1", "patron1@example.com", "1234567890", LocalDate.now(), null);
        patron2 = new Patron(2L, "Patron 2", "patron2@example.com", "0987654321", LocalDate.now(), null);
    }

    @Test
    public void testGetAllPatrons() throws Exception {
        List<Patron> patrons = Arrays.asList(patron1, patron2);

        when(patronService.getAllPatrons()).thenReturn(patrons);

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Patron 1"))
                .andExpect(jsonPath("$[1].name").value("Patron 2"));
    }

    @Test
    public void testGetPatronById() throws Exception {
        when(patronService.getPatronById(1L)).thenReturn(patron1);

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Patron 1"));
    }

    @Test
    public void testGetPatronByIdNotFound() throws Exception {
        when(patronService.getPatronById(1L)).thenThrow(new NotFoundException("Patron with ID 1 not found"));

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddPatron() throws Exception {
        when(patronService.addPatron(any(Patron.class))).thenReturn("Patron added successfully");

        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Patron added successfully"));
    }

    @Test
    public void testUpdatePatron() throws Exception {
        when(patronService.updatePatron(eq(1L), any(Patron.class))).thenReturn("Patron updated successfully");

        mockMvc.perform(put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Patron updated successfully"));
    }

    @Test
    public void testUpdatePatronNotFound() throws Exception {
        when(patronService.updatePatron(eq(1L), any(Patron.class))).thenThrow(new NotFoundException("Patron with ID 1 not found"));

        mockMvc.perform(put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletePatron() throws Exception {
        when(patronService.deletePatron(1L)).thenReturn("Patron deleted successfully");

        mockMvc.perform(delete("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patron deleted successfully"));
    }

    @Test
    public void testDeletePatronNotFound() throws Exception {
        when(patronService.deletePatron(1L)).thenThrow(new NotFoundException("Patron with ID 1 not found"));

        mockMvc.perform(delete("/api/patrons/1"))
                .andExpect(status().isNotFound());
    }
}