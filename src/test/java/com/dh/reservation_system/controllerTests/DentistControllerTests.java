package com.dh.reservation_system.controllerTests;

import com.dh.reservation_system.controllers.DentistController;
import com.dh.reservation_system.dtos.DentistRequestDto;
import com.dh.reservation_system.dtos.DentistRequestToUpdateDto;
import com.dh.reservation_system.dtos.DentistResponseDto;
import com.dh.reservation_system.services.impl.DentistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DentistController.class)
public class DentistControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DentistService dentistService;

    private DentistResponseDto sampleDentistResponseDto;
    private DentistRequestDto sampleDentistRequestDto;

    @BeforeEach
    public void setUp() {
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
    public void testSaveDentist() throws Exception {
        when(dentistService.save(any(DentistRequestDto.class))).thenReturn(sampleDentistResponseDto);

        mockMvc.perform(post("/dentists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sampleDentistRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.licenseMedical").value("ABC123"));

        verify(dentistService, times(1)).save(any(DentistRequestDto.class));
    }

    @Test
    public void testFindAllDentists() throws Exception {

        when(dentistService.findAll()).thenReturn(List.of(sampleDentistResponseDto));

        mockMvc.perform(get("/dentists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));

        verify(dentistService, times(1)).findAll();
    }

    @Test
    public void testFindDentistById() throws Exception {
        when(dentistService.findById(1L)).thenReturn(sampleDentistResponseDto);

        mockMvc.perform(get("/dentists/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(dentistService, times(1)).findById(1L);
    }

    @Test
    public void testFindDentistByLicenseMedical() throws Exception {
        when(dentistService.findByLicenseMedical("ABC123")).thenReturn(sampleDentistResponseDto);

        mockMvc.perform(get("/dentists/licenseMedical/ABC123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.licenseMedical").value("ABC123"));

        verify(dentistService, times(1)).findByLicenseMedical("ABC123");
    }

    @Test
    public void testUpdateDentist() throws Exception {
        when(dentistService.update(any(DentistRequestToUpdateDto.class))).thenReturn(sampleDentistResponseDto);

        DentistRequestToUpdateDto updateDto = new DentistRequestToUpdateDto();
        updateDto.setId(1L);
        updateDto.setName("Jane");
        updateDto.setLastName("Smith");
        updateDto.setLicenseMedical("XYZ456");

        mockMvc.perform(put("/dentists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))  // Mocking still returns the same response
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.licenseMedical").value("ABC123"));

        verify(dentistService, times(1)).update(any(DentistRequestToUpdateDto.class));
    }

    @Test
    public void testDeleteDentist() throws Exception {
        doNothing().when(dentistService).delete(1L);

        mockMvc.perform(delete("/dentists/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(dentistService, times(1)).delete(1L);
    }
}
