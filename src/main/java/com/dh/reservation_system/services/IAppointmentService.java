package com.dh.reservation_system.services;

import com.dh.reservation_system.entities.Appointment;

import java.util.List;

public interface IAppointmentService {
    Appointment save(Appointment appointment);
    List<Appointment> findAll();
    Appointment findById(Long id);
}
