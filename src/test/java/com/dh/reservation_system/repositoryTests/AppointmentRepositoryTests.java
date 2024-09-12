package com.dh.reservation_system.repositoryTests;

import com.dh.reservation_system.entities.Address;
import com.dh.reservation_system.entities.Appointment;
import com.dh.reservation_system.entities.Dentist;
import com.dh.reservation_system.entities.Patient;
import com.dh.reservation_system.repositories.IAppointmentRepository;
import com.dh.reservation_system.repositories.IDentistRepository;
import com.dh.reservation_system.repositories.IPatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AppointmentRepositoryTests {
    @Autowired
    private IAppointmentRepository appointmentRepository;

    @Autowired
    private IPatientRepository patientRepository;

    @Autowired
    private IDentistRepository dentistRepository;

    private Patient samplePatient;
    private Dentist sampleDentist;
    private Appointment sampleAppointment;

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
        patientRepository.save(samplePatient);

        sampleDentist = new Dentist();
        sampleDentist.setName("Dr. Smith");
        sampleDentist.setLastName("Johnson");
        sampleDentist.setLicenseMedical("ABC123");
        dentistRepository.save(sampleDentist);

        sampleAppointment = new Appointment();
        sampleAppointment.setDate(LocalDateTime.of(2024, 9, 11, 14, 30));
        sampleAppointment.setPatient(samplePatient);
        sampleAppointment.setDentist(sampleDentist);
    }

    @Test
    public void testCreateAppointment() {
        Appointment savedAppointment = appointmentRepository.save(sampleAppointment);

        assertNotNull(savedAppointment.getId());
        assertEquals(sampleAppointment.getDate(), savedAppointment.getDate());
        assertEquals(samplePatient.getId(), savedAppointment.getPatient().getId());
        assertEquals(sampleDentist.getId(), savedAppointment.getDentist().getId());
    }

    @Test
    public void testFindAppointmentById() {
        Appointment savedAppointment = appointmentRepository.save(sampleAppointment);
        Optional<Appointment> foundAppointment = appointmentRepository.findById(savedAppointment.getId());

        assertTrue(foundAppointment.isPresent());
        assertEquals(savedAppointment.getId(), foundAppointment.get().getId());
        assertEquals(savedAppointment.getPatient().getId(), foundAppointment.get().getPatient().getId());
        assertEquals(savedAppointment.getDentist().getId(), foundAppointment.get().getDentist().getId());
    }

    @Test
    public void testUpdateAppointment() {
        Appointment savedAppointment = appointmentRepository.save(sampleAppointment);

        savedAppointment.setDate(LocalDateTime.of(2024, 10, 15, 10, 0));
        Appointment updatedAppointment = appointmentRepository.save(savedAppointment);

        assertEquals(LocalDateTime.of(2024, 10, 15, 10, 0), updatedAppointment.getDate());
        assertEquals(samplePatient.getId(), updatedAppointment.getPatient().getId());
        assertEquals(sampleDentist.getId(), updatedAppointment.getDentist().getId());
    }

    @Test
    public void testDeleteAppointment() {
        Appointment savedAppointment = appointmentRepository.save(sampleAppointment);

        appointmentRepository.deleteById(savedAppointment.getId());

        Optional<Appointment> deletedAppointment = appointmentRepository.findById(savedAppointment.getId());
        assertFalse(deletedAppointment.isPresent());
    }

    @Test
    public void testFindAllAppointments() {
        Appointment appointment1 = new Appointment();
        appointment1.setDate(LocalDateTime.of(2024, 9, 12, 9, 0));
        appointment1.setPatient(samplePatient);
        appointment1.setDentist(sampleDentist);

        Appointment appointment2 = new Appointment();
        appointment2.setDate(LocalDateTime.of(2024, 9, 13, 11, 0));
        appointment2.setPatient(samplePatient);
        appointment2.setDentist(sampleDentist);

        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);

        List<Appointment> appointments = appointmentRepository.findAll();

        assertEquals(2, appointments.size());
    }
}
