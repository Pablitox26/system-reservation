package com.dh.reservation_system.serviceTests;

import com.dh.reservation_system.dtos.AppointmentRequestDto;
import com.dh.reservation_system.dtos.AppointmentResponseDto;
import com.dh.reservation_system.entities.Appointment;
import com.dh.reservation_system.entities.Dentist;
import com.dh.reservation_system.entities.Patient;
import com.dh.reservation_system.repositories.IAppointmentRepository;
import com.dh.reservation_system.repositories.IDentistRepository;
import com.dh.reservation_system.repositories.IPatientRepository;
import com.dh.reservation_system.services.impl.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTests {
    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private IAppointmentRepository appointmentRepository;

    @Mock
    private IDentistRepository dentistRepository;

    @Mock
    private IPatientRepository patientRepository;

    @Mock
    private ObjectMapper objectMapper;

    private Appointment sampleAppointment;
    private AppointmentResponseDto sampleAppointmentResponseDto;
    private AppointmentRequestDto sampleAppointmentRequestDto;
    private Dentist sampleDentist;
    private Patient samplePatient;

    @BeforeEach
    public void setUp() {
        sampleDentist = new Dentist();
        sampleDentist.setId(1L);
        sampleDentist.setName("Dr. Smith");
        sampleDentist.setLicenseMedical("ABC123");

        samplePatient = new Patient();
        samplePatient.setId(1L);
        samplePatient.setName("John");
        samplePatient.setLastName("Doe");
        samplePatient.setDni("12345678");

        sampleAppointment = new Appointment();
        sampleAppointment.setId(1L);
        sampleAppointment.setDate(LocalDateTime.of(2024, 9, 11, 14, 30));
        sampleAppointment.setDentist(sampleDentist);
        sampleAppointment.setPatient(samplePatient);

        sampleAppointmentResponseDto = new AppointmentResponseDto();
        sampleAppointmentResponseDto.setId(1L);
        sampleAppointmentResponseDto.setDate("2024-09-11T14:30");

        sampleAppointmentRequestDto = new AppointmentRequestDto();
        sampleAppointmentRequestDto.setDate(LocalDateTime.of(2024, 9, 11, 14, 30));
        sampleAppointmentRequestDto.setDentistId(1L);
        sampleAppointmentRequestDto.setPatientId(1L);
    }

    @Test
    public void testSaveAppointment() {
        when(dentistRepository.findById(1L)).thenReturn(Optional.of(sampleDentist));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(sampleAppointment);
        when(objectMapper.convertValue(sampleAppointment, AppointmentResponseDto.class)).thenReturn(sampleAppointmentResponseDto);

        AppointmentResponseDto response = appointmentService.save(sampleAppointmentRequestDto);

        assertEquals(sampleAppointmentResponseDto.getId(), response.getId());
        assertEquals(sampleAppointmentResponseDto.getDate(), response.getDate());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    public void testFindAllAppointments() {
        when(appointmentRepository.findAll()).thenReturn(List.of(sampleAppointment));
        when(objectMapper.convertValue(sampleAppointment, AppointmentResponseDto.class)).thenReturn(sampleAppointmentResponseDto);

        List<AppointmentResponseDto> appointments = appointmentService.findAll();

        assertEquals(1, appointments.size());
        assertEquals(sampleAppointmentResponseDto.getId(), appointments.get(0).getId());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(sampleAppointment));
        when(objectMapper.convertValue(sampleAppointment, AppointmentResponseDto.class)).thenReturn(sampleAppointmentResponseDto);

        AppointmentResponseDto response = appointmentService.findById(1L);

        assertEquals(sampleAppointmentResponseDto.getId(), response.getId());
        verify(appointmentRepository, times(1)).findById(1L);
    }
}
