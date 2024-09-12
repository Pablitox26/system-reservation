package com.dh.reservation_system.controllerTests;

import com.dh.reservation_system.controllers.AppointmentController;
import com.dh.reservation_system.dtos.AppointmentRequestDto;
import com.dh.reservation_system.dtos.AppointmentResponseDto;
import com.dh.reservation_system.services.impl.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    private AppointmentResponseDto sampleAppointmentResponseDto;
    private AppointmentRequestDto sampleAppointmentRequestDto;

    @BeforeEach
    public void setUp() {
        sampleAppointmentResponseDto = new AppointmentResponseDto();
        sampleAppointmentResponseDto.setId(1L);
        sampleAppointmentResponseDto.setDate("2024-09-11T14:30:00");

        sampleAppointmentRequestDto = new AppointmentRequestDto();
        sampleAppointmentRequestDto.setDate(LocalDateTime.of(2024, 9, 11, 14, 30));
        sampleAppointmentRequestDto.setPatientId(1L);
        sampleAppointmentRequestDto.setDentistId(1L);
    }

    @Test
    public void testSaveAppointment() throws Exception {
        when(appointmentService.save(any(AppointmentRequestDto.class))).thenReturn(sampleAppointmentResponseDto);

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sampleAppointmentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").value("2024-09-11T14:30:00"));

        verify(appointmentService, times(1)).save(any(AppointmentRequestDto.class));
    }

    @Test
    public void testFindAllAppointments() throws Exception {
        when(appointmentService.findAll()).thenReturn(List.of(sampleAppointmentResponseDto));

        mockMvc.perform(get("/appointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].date").value("2024-09-11T14:30:00"));

        verify(appointmentService, times(1)).findAll();
    }

    @Test
    public void testFindAppointmentById() throws Exception {
        when(appointmentService.findById(1L)).thenReturn(sampleAppointmentResponseDto);

        mockMvc.perform(get("/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").value("2024-09-11T14:30:00"));

        verify(appointmentService, times(1)).findById(1L);
    }
}
