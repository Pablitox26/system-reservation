package com.dh.reservation_system.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponseDto {
    private Long id;
    private String dentist;
    private String patient;
    private String date;
}
