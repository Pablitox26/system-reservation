package com.dh.reservation_system.repositoryTests;

import com.dh.reservation_system.entities.Dentist;
import com.dh.reservation_system.repositories.IDentistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DentistRepositoryTests {
    @Autowired
    private IDentistRepository dentistRepository;

    private Dentist sampleDentist;

    @BeforeEach
    public void setUp() {
        sampleDentist = new Dentist();
        sampleDentist.setName("John");
        sampleDentist.setLastName("Doe");
        sampleDentist.setLicenseMedical("ABC123");
    }

    @Test
    public void testCreateDentist() {
        Dentist savedDentist = dentistRepository.save(sampleDentist);

        assertNotNull(savedDentist.getId());
        assertEquals(sampleDentist.getName(), savedDentist.getName());
        assertEquals(sampleDentist.getLicenseMedical(), savedDentist.getLicenseMedical());
    }

    @Test
    public void testFindDentistById() {
        Dentist savedDentist = dentistRepository.save(sampleDentist);
        Optional<Dentist> foundDentist = dentistRepository.findById(savedDentist.getId());

        assertTrue(foundDentist.isPresent());
        assertEquals(savedDentist.getId(), foundDentist.get().getId());
        assertEquals(savedDentist.getLicenseMedical(), foundDentist.get().getLicenseMedical());
    }

    @Test
    public void testUpdateDentist() {
        Dentist savedDentist = dentistRepository.save(sampleDentist);

        savedDentist.setName("Jane");
        Dentist updatedDentist = dentistRepository.save(savedDentist);

        assertEquals("Jane", updatedDentist.getName());
        assertEquals(sampleDentist.getLicenseMedical(), updatedDentist.getLicenseMedical());
    }

    @Test
    public void testDeleteDentist() {
        Dentist savedDentist = dentistRepository.save(sampleDentist);

        dentistRepository.deleteById(savedDentist.getId());

        Optional<Dentist> deletedDentist = dentistRepository.findById(savedDentist.getId());
        assertFalse(deletedDentist.isPresent());
    }

    @Test
    public void testFindByLicenseMedical() {
        dentistRepository.save(sampleDentist);

        Optional<Dentist> foundDentist = dentistRepository.findByLicenseMedical("ABC123");

        assertTrue(foundDentist.isPresent());
        assertEquals(sampleDentist.getLicenseMedical(), foundDentist.get().getLicenseMedical());
    }

    @Test
    public void testFindByLicenseMedical_NotFound() {
        Optional<Dentist> foundDentist = dentistRepository.findByLicenseMedical("NONEXISTENT");

        assertFalse(foundDentist.isPresent());
    }

    @Test
    public void testFindAllDentists() {
        Dentist dentist1 = new Dentist();
        dentist1.setName("Alice");
        dentist1.setLastName("Smith");
        dentist1.setLicenseMedical("DEF456");

        Dentist dentist2 = new Dentist();
        dentist2.setName("Bob");
        dentist2.setLastName("Johnson");
        dentist2.setLicenseMedical("GHI789");

        dentistRepository.save(dentist1);
        dentistRepository.save(dentist2);

        List<Dentist> dentists = dentistRepository.findAll();

        assertEquals(2, dentists.size());
    }
}
