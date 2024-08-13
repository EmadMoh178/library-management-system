package com.example.library_management.service;

import com.example.library_management.model.Patron;
import com.example.library_management.repository.PatronRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatronService {

    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public Optional<Patron> getPatronById(Long id) {
        return patronRepository.findById(id);
    }

    public String addPatron(Patron patron) {
        patronRepository.save(patron);
        return "Patron added successfully";
    }

    public String updatePatron(Long id, Patron updatedPatron) {
        return patronRepository.findById(id).map(patron -> {
            patron.setName(updatedPatron.getName());
            patron.setEmail(updatedPatron.getEmail());
            patron.setPhoneNumber(updatedPatron.getPhoneNumber());
            patron.setMembershipDate(updatedPatron.getMembershipDate());
            patronRepository.save(patron);
            return "Patron updated successfully";
        }).orElse("Patron with ID " + id + " not found");
    }

    public String deletePatron(Long id) {
        return patronRepository.findById(id).map(patron -> {
            patronRepository.delete(patron);
            return "Patron deleted successfully";
        }).orElse("Patron with ID " + id + " not found");
    }
}