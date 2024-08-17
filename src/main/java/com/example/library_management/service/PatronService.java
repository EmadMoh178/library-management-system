package com.example.library_management.service;

import com.example.library_management.exception.NotFoundException;
import com.example.library_management.model.Patron;
import com.example.library_management.repository.PatronRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @Cacheable(value = "patrons", key = "'allPatrons'")
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "patrons", key = "#id")
    public Patron getPatronById(Long id) {
        return patronRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patron with ID " + id + " not found"));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "patrons", key = "'allPatrons'"),
            @CacheEvict(value = "patrons", allEntries = true)
    })
    public String addPatron(Patron patron) {
        patronRepository.save(patron);
        return "Patron added successfully";
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "patrons", key = "'allPatrons'"),
            @CacheEvict(value = "patrons", key = "#id")
    })
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
    @Caching(evict = {
            @CacheEvict(value = "patrons", key = "'allPatrons'"),
            @CacheEvict(value = "patrons", key = "#id")
    })
    public String deletePatron(Long id) {
        return patronRepository.findById(id).map(patron -> {
            patronRepository.delete(patron);
            return "Patron deleted successfully";
        }).orElseThrow(() -> new NotFoundException("Patron with ID " + id + " not found"));
    }
}
