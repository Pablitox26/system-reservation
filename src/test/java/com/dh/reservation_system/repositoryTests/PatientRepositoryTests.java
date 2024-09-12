package com.dh.reservation_system.repositoryTests;

import com.dh.reservation_system.entities.Address;
import com.dh.reservation_system.entities.Patient;
import com.dh.reservation_system.repositories.IPatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PatientRepositoryTests {

    @Autowired
    private IPatientRepository patientRepository;

    private Patient samplePatient;

    @BeforeEach
    public void setUp() {
        samplePatient = new Patient();
        samplePatient.setName("John");
        samplePatient.setLastName("Doe");
        samplePatient.setDni("12345678");
        samplePatient.setDischargeDate(LocalDate.of(2024, 9, 11));

        Address address = new Address();
        address.setStreet("Main Street");
        address.setNumber(1234);
        address.setLocation("New York");
        address.setProvince("New York");
        samplePatient.setAddress(address);
    }

    @Test
    public void testCreatePatient() {
        Patient savedPatient = patientRepository.save(samplePatient);

        assertNotNull(savedPatient.getId());
        assertEquals(samplePatient.getName(), savedPatient.getName());
        assertEquals(samplePatient.getDni(), savedPatient.getDni());
    }

    @Test
    public void testFindPatientById() {
        Patient savedPatient = patientRepository.save(samplePatient);
        Optional<Patient> foundPatient = patientRepository.findById(savedPatient.getId());

        assertTrue(foundPatient.isPresent());
        assertEquals(savedPatient.getId(), foundPatient.get().getId());
        assertEquals(savedPatient.getName(), foundPatient.get().getName());
        assertEquals(savedPatient.getDni(), foundPatient.get().getDni());
    }

    @Test
    public void testUpdatePatient() {
        Patient savedPatient = patientRepository.save(samplePatient);

        savedPatient.setName("Jane");
        Patient updatedPatient = patientRepository.save(savedPatient);

        assertEquals("Jane", updatedPatient.getName());
        assertEquals(samplePatient.getDni(), updatedPatient.getDni());
    }

    @Test
    public void testDeletePatient() {
        Patient savedPatient = patientRepository.save(samplePatient);

        patientRepository.deleteById(savedPatient.getId());

        Optional<Patient> deletedPatient = patientRepository.findById(savedPatient.getId());
        assertFalse(deletedPatient.isPresent());
    }

    @Test
    public void testFindAllPatients() {
        Patient patient1 = new Patient();
        patient1.setName("Alice");
        patient1.setLastName("Smith");
        patient1.setDni("98765432");
        patient1.setDischargeDate(LocalDate.of(2024, 9, 12));

        Patient patient2 = new Patient();
        patient2.setName("Bob");
        patient2.setLastName("Johnson");
        patient2.setDni("87654321");
        patient2.setDischargeDate(LocalDate.of(2024, 9, 13));

        patientRepository.save(patient1);
        patientRepository.save(patient2);

        List<Patient> patients = patientRepository.findAll();

        assertEquals(2, patients.size());
    }
}
