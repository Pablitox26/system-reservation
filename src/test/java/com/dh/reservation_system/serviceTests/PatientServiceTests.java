package com.dh.reservation_system.serviceTests;

import com.dh.reservation_system.dtos.PatientRequestDto;
import com.dh.reservation_system.dtos.PatientRequestToUpdateDto;
import com.dh.reservation_system.dtos.PatientResponseDto;
import com.dh.reservation_system.entities.Patient;
import com.dh.reservation_system.repositories.IPatientRepository;
import com.dh.reservation_system.services.impl.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTests {
    @InjectMocks
    private PatientService patientService;

    @Mock
    private IPatientRepository patientRepository;

    @Mock
    private ObjectMapper objectMapper;

    private Patient samplePatient;
    private PatientResponseDto samplePatientResponseDto;
    private PatientRequestDto samplePatientRequestDto;

    @BeforeEach
    public void setUp() {
        samplePatient = new Patient();
        samplePatient.setId(1L);
        samplePatient.setName("John");
        samplePatient.setLastName("Doe");
        samplePatient.setDni("12345678");
        samplePatient.setDischargeDate(LocalDate.of(2024, 9, 11));

        samplePatientResponseDto = new PatientResponseDto();
        samplePatientResponseDto.setId(1L);
        samplePatientResponseDto.setName("John");
        samplePatientResponseDto.setLastName("Doe");
        samplePatientResponseDto.setDni("12345678");
        samplePatientResponseDto.setDischargeDate("2024-09-11");

        samplePatientRequestDto = new PatientRequestDto();
        samplePatientRequestDto.setName("John");
        samplePatientRequestDto.setLastName("Doe");
        samplePatientRequestDto.setDni("12345678");
    }

    @Test
    public void testSavePatient() {
        when(objectMapper.convertValue(samplePatientRequestDto, Patient.class)).thenReturn(samplePatient);
        when(objectMapper.convertValue(samplePatient, PatientResponseDto.class)).thenReturn(samplePatientResponseDto);

        when(patientRepository.save(samplePatient)).thenReturn(samplePatient);

        PatientResponseDto response = patientService.save(samplePatientRequestDto);

        assertEquals(samplePatientResponseDto.getName(), response.getName());
        verify(patientRepository, times(1)).save(samplePatient);
    }

    @Test
    public void testFindAllPatients() {
        when(patientRepository.findAll()).thenReturn(List.of(samplePatient));
        when(objectMapper.convertValue(samplePatient, PatientResponseDto.class)).thenReturn(samplePatientResponseDto);

        List<PatientResponseDto> patients = patientService.findAll();

        assertEquals(1, patients.size());
        assertEquals(samplePatientResponseDto.getName(), patients.get(0).getName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient));
        when(objectMapper.convertValue(samplePatient, PatientResponseDto.class)).thenReturn(samplePatientResponseDto);

        PatientResponseDto response = patientService.findById(1L);

        assertEquals(samplePatientResponseDto.getId(), response.getId());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdatePatient() {
        PatientRequestToUpdateDto updateDto = new PatientRequestToUpdateDto();
        updateDto.setId(1L);
        updateDto.setName("Jane");
        updateDto.setLastName("Smith");
        updateDto.setDni("98765432");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient));
        when(objectMapper.convertValue(updateDto, Patient.class)).thenReturn(samplePatient);
        when(objectMapper.convertValue(samplePatient, PatientResponseDto.class)).thenReturn(samplePatientResponseDto);
        when(patientRepository.save(samplePatient)).thenReturn(samplePatient);

        PatientResponseDto response = patientService.update(updateDto);

        assertEquals(samplePatientResponseDto.getName(), response.getName());
        verify(patientRepository, times(1)).save(samplePatient);
    }

    @Test
    public void testDeletePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient));

        patientService.delete(1L);

        verify(patientRepository, times(1)).deleteById(1L);
    }
}
