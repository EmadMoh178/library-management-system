package com.example.library_management.service;

import com.example.library_management.exception.NotFoundException;
import com.example.library_management.model.Patron;
import com.example.library_management.repository.PatronRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatronService {

    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Transactional(readOnly = true)
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Patron getPatronById(Long id) {
        return patronRepository.findById(id).orElseThrow(() -> new NotFoundException("Patron with ID " + id + " not found"));
    }

    @Transactional
    public String addPatron(Patron patron) {
        patronRepository.save(patron);
        return "Patron added successfully";
    }

    @Transactional
    public String updatePatron(Long id, Patron updatedPatron) {
        return patronRepository.findById(id).map(patron -> {
            patron.setName(updatedPatron.getName());
            patron.setEmail(updatedPatron.getEmail());
            patron.setPhoneNumber(updatedPatron.getPhoneNumber());
            patron.setMembershipDate(updatedPatron.getMembershipDate());
            patronRepository.save(patron);
            return "Patron updated successfully";
        }).orElseThrow(() -> new NotFoundException("Patron with ID " + id + " not found"));
    }

    @Transactional
    public String deletePatron(Long id) {
        return patronRepository.findById(id).map(patron -> {
            patronRepository.delete(patron);
            return "Patron deleted successfully";
        }).orElseThrow(() -> new NotFoundException("Patron with ID " + id + " not found"));
    }
}