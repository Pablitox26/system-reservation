package com.dh.reservation_system.services;

import com.dh.reservation_system.dtos.AppointmentRequestDto;
import com.dh.reservation_system.dtos.AppointmentResponseDto;

import java.util.List;

public interface IAppointmentService {
    AppointmentResponseDto save(AppointmentRequestDto appointmentDto);
    List<AppointmentResponseDto> findAll();
    AppointmentResponseDto findById(Long id);
}
