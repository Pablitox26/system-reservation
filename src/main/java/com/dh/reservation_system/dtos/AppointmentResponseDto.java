package com.dh.reservation_system.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponseDto {
    private Long id;
    private String date;
    private PatientResponseDto patient;
    private DentistResponseDto dentist;
}
