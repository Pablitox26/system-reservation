package com.dh.reservation_system.serviceTests;

import com.dh.reservation_system.dtos.DentistRequestDto;
import com.dh.reservation_system.dtos.DentistRequestToUpdateDto;
import com.dh.reservation_system.dtos.DentistResponseDto;
import com.dh.reservation_system.entities.Dentist;
import com.dh.reservation_system.repositories.IDentistRepository;
import com.dh.reservation_system.services.impl.DentistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DentistServiceTests {
    @InjectMocks
    private DentistService dentistService;

    @Mock
    private IDentistRepository dentistRepository;

    @Mock
    private ObjectMapper objectMapper;

    private Dentist sampleDentist;
    private DentistResponseDto sampleDentistResponseDto;
    private DentistRequestDto sampleDentistRequestDto;

    @BeforeEach
    public void setUp() {
        sampleDentist = new Dentist();
        sampleDentist.setId(1L);
        sampleDentist.setName("John");
        sampleDentist.setLastName("Doe");
        sampleDentist.setLicenseMedical("ABC123");

        sampleDentistResponseDto = new DentistResponseDto();
        sampleDentistResponseDto.setId(1L);
        sampleDentistResponseDto.setName("John");
        sampleDentistResponseDto.setLastName("Doe");
        sampleDentistResponseDto.setLicenseMedical("ABC123");

        sampleDentistRequestDto = new DentistRequestDto();
        sampleDentistRequestDto.setName("John");
        sampleDentistRequestDto.setLastName("Doe");
        sampleDentistRequestDto.setLicenseMedical("ABC123");
    }

    @Test
    public void testSaveDentist() {
        // Mock the ObjectMapper to convert DTO to Entity and vice versa
        when(objectMapper.convertValue(sampleDentistRequestDto, Dentist.class)).thenReturn(sampleDentist);
        when(objectMapper.convertValue(sampleDentist, DentistResponseDto.class)).thenReturn(sampleDentistResponseDto);

        // Mock the repository save operation
        when(dentistRepository.save(sampleDentist)).thenReturn(sampleDentist);

        // Call the service method
        DentistResponseDto response = dentistService.save(sampleDentistRequestDto);

        // Verify and assert the results
        assertEquals(sampleDentistResponseDto.getName(), response.getName());
        verify(dentistRepository, times(1)).save(sampleDentist);
    }

    @Test
    public void testFindAllDentists() {
        // Mock the repository findAll() method
        when(dentistRepository.findAll()).thenReturn(List.of(sampleDentist));
        when(objectMapper.convertValue(sampleDentist, DentistResponseDto.class)).thenReturn(sampleDentistResponseDto);

        // Call the service method
        List<DentistResponseDto> dentists = dentistService.findAll();

        // Verify and assert
        assertEquals(1, dentists.size());
        assertEquals(sampleDentistResponseDto.getName(), dentists.get(0).getName());
        verify(dentistRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        // Mock the repository findById() method
        when(dentistRepository.findById(1L)).thenReturn(Optional.of(sampleDentist));
        when(objectMapper.convertValue(sampleDentist, DentistResponseDto.class)).thenReturn(sampleDentistResponseDto);

        // Call the service method
        DentistResponseDto response = dentistService.findById(1L);

        // Verify and assert
        assertEquals(sampleDentistResponseDto.getId(), response.getId());
        verify(dentistRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByLicenseMedical() {
        // Mock the repository findByLicenseMedical() method
        when(dentistRepository.findByLicenseMedical("ABC123")).thenReturn(Optional.of(sampleDentist));
        when(objectMapper.convertValue(sampleDentist, DentistResponseDto.class)).thenReturn(sampleDentistResponseDto);

        // Call the service method
        DentistResponseDto response = dentistService.findByLicenseMedical("ABC123");

        // Verify and assert
        assertEquals(sampleDentistResponseDto.getLicenseMedical(), response.getLicenseMedical());
        verify(dentistRepository, times(1)).findByLicenseMedical("ABC123");
    }

    @Test
    public void testUpdateDentist() {
        // Mock the findById and save methods
        DentistRequestToUpdateDto updateDto = new DentistRequestToUpdateDto();
        updateDto.setId(1L);
        updateDto.setName("Jane");
        updateDto.setLastName("Smith");
        updateDto.setLicenseMedical("ABC123");

        when(dentistRepository.findById(1L)).thenReturn(Optional.of(sampleDentist));
        when(objectMapper.convertValue(updateDto, Dentist.class)).thenReturn(sampleDentist);
        when(objectMapper.convertValue(sampleDentist, DentistResponseDto.class)).thenReturn(sampleDentistResponseDto);
        when(dentistRepository.save(sampleDentist)).thenReturn(sampleDentist);

        // Call the service method
        DentistResponseDto response = dentistService.update(updateDto);

        // Verify and assert
        assertEquals(sampleDentistResponseDto.getName(), response.getName());
        verify(dentistRepository, times(1)).save(sampleDentist);
    }

    @Test
    public void testDeleteDentist() {
        // Mock the findById and deleteById methods
        when(dentistRepository.findById(1L)).thenReturn(Optional.of(sampleDentist));

        // Call the service method
        dentistService.delete(1L);

        // Verify delete operation
        verify(dentistRepository, times(1)).deleteById(1L);
    }
}
