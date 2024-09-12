package com.dh.reservation_system.controllerTests;

import com.dh.reservation_system.controllers.PatientController;
import com.dh.reservation_system.dtos.*;
import com.dh.reservation_system.services.impl.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
public class PatientControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    private PatientResponseDto samplePatientResponseDto;
    private PatientRequestDto samplePatientRequestDto;
    private AddressResponseDto sampleAddressResponseDto;
    private AddressRequestDto sampleAddressRequestDto;

    @BeforeEach
    public void setUp() {
        sampleAddressResponseDto = new AddressResponseDto();
        sampleAddressResponseDto.setStreet("123 Main St");
        sampleAddressResponseDto.setLocation("Springfield");
        sampleAddressResponseDto.setNumber(123);
        sampleAddressResponseDto.setProvince("Springfield");

        sampleAddressRequestDto = new AddressRequestDto();
        sampleAddressRequestDto.setStreet("123 Main St");
        sampleAddressResponseDto.setLocation("Springfield");
        sampleAddressResponseDto.setNumber(123);
        sampleAddressResponseDto.setProvince("Springfield");

        samplePatientResponseDto = new PatientResponseDto();
        samplePatientResponseDto.setId(1L);
        samplePatientResponseDto.setName("John");
        samplePatientResponseDto.setLastName("Doe");
        samplePatientResponseDto.setDni("12345678");
        samplePatientResponseDto.setDischargeDate("2024-09-11");
        samplePatientResponseDto.setAddress(sampleAddressResponseDto);

        samplePatientRequestDto = new PatientRequestDto();
        samplePatientRequestDto.setName("John");
        samplePatientRequestDto.setLastName("Doe");
        samplePatientRequestDto.setDni("12345678");
        samplePatientRequestDto.setDischargeDate(LocalDate.of(2024, 9, 11));
        samplePatientRequestDto.setAddress(sampleAddressRequestDto);
    }

    @Test
    public void testSavePatient() throws Exception {
        when(patientService.save(any(PatientRequestDto.class))).thenReturn(samplePatientResponseDto);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(samplePatientRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.dischargeDate").value("2024-09-11"))
                .andExpect(jsonPath("$.address.street").value("123 Main St"))
                .andExpect(jsonPath("$.address.number").value(123))
                .andExpect(jsonPath("$.address.location").value("Springfield"))
                .andExpect(jsonPath("$.address.province").value("Springfield"));

        verify(patientService, times(1)).save(any(PatientRequestDto.class));
    }

    @Test
    public void testFindAllPatients() throws Exception {
        when(patientService.findAll()).thenReturn(List.of(samplePatientResponseDto));

        mockMvc.perform(get("/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].dni").value("12345678"))
                .andExpect(jsonPath("$[0].dischargeDate").value("2024-09-11"))
                .andExpect(jsonPath("$[0].address.street").value("123 Main St"))
                .andExpect(jsonPath("$[0].address.number").value(123))
                .andExpect(jsonPath("$[0].address.location").value("Springfield"))
                .andExpect(jsonPath("$[0].address.province").value("Springfield"));


        verify(patientService, times(1)).findAll();
    }

    @Test
    public void testFindPatientById() throws Exception {
        when(patientService.findById(1L)).thenReturn(samplePatientResponseDto);

        mockMvc.perform(get("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.dischargeDate").value("2024-09-11"))
                .andExpect(jsonPath("$.address.street").value("123 Main St"))
                .andExpect(jsonPath("$.address.number").value(123))
                .andExpect(jsonPath("$.address.location").value("Springfield"))
                .andExpect(jsonPath("$.address.province").value("Springfield"));

        verify(patientService, times(1)).findById(1L);
    }

    @Test
    public void testUpdatePatient() throws Exception {
        when(patientService.update(any(PatientRequestToUpdateDto.class))).thenReturn(samplePatientResponseDto);

        PatientRequestToUpdateDto updateDto = new PatientRequestToUpdateDto();
        updateDto.setId(1L);
        updateDto.setName("Jane");
        updateDto.setLastName("Smith");
        updateDto.setDni("87654321");
        updateDto.setDischargeDate("2024-10-11");

        mockMvc.perform(put("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))  // Mocking still returns the same response
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.dischargeDate").value("2024-09-11"))
                .andExpect(jsonPath("$.address.street").value("123 Main St"))
                .andExpect(jsonPath("$.address.number").value(123))
                .andExpect(jsonPath("$.address.location").value("Springfield"))
                .andExpect(jsonPath("$.address.province").value("Springfield"));

        verify(patientService, times(1)).update(any(PatientRequestToUpdateDto.class));
    }

    @Test
    public void testDeletePatient() throws Exception {
        doNothing().when(patientService).delete(1L);

        mockMvc.perform(delete("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(patientService, times(1)).delete(1L);
    }
}
