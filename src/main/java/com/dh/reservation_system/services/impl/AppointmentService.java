package com.dh.reservation_system.services.impl;

import com.dh.reservation_system.dtos.AppointmentRequestDto;
import com.dh.reservation_system.dtos.AppointmentResponseDto;
import com.dh.reservation_system.dtos.PatientResponseDto;
import com.dh.reservation_system.entities.Appointment;
import com.dh.reservation_system.entities.Dentist;
import com.dh.reservation_system.entities.Patient;
import com.dh.reservation_system.exceptions.NotFoundException;
import com.dh.reservation_system.repositories.IAppointmentRepository;
import com.dh.reservation_system.repositories.IDentistRepository;
import com.dh.reservation_system.repositories.IPatientRepository;
import com.dh.reservation_system.services.IAppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {
    private static final Logger logger = Logger.getLogger(AppointmentService.class);
    private final IAppointmentRepository appointmentRepository;
    private final IDentistRepository dentistRepository;
    private final IPatientRepository patientRepository;
    private final ObjectMapper objectMapper;

    public AppointmentService(
            IAppointmentRepository appointmentRepository,
            IDentistRepository dentistRepository,
            IPatientRepository patientRepository,
            ObjectMapper objectMapper) {
        this.appointmentRepository = appointmentRepository;
        this.dentistRepository = dentistRepository;
        this.patientRepository = patientRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public AppointmentResponseDto save(AppointmentRequestDto appointmentDto) {
        logger.info("Saving appointment: " + appointmentDto.getDate());
        Appointment appointment = mapToEntity(appointmentDto);
        appointment = appointmentRepository.save(appointment);
        logger.info("Appointment saved: " + appointment.getId());
        return mapToDto(appointment);
    }

    @Override
    public List<AppointmentResponseDto> findAll() {
        logger.info("Finding all appointments");
        List<AppointmentResponseDto> appointments = appointmentRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
        logger.info("Found " + appointments.size() + " appointments");
        return appointments;
    }

    @Override
    public AppointmentResponseDto findById(Long id) {
        logger.info("Finding appointment by id: " + id);
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Appointment not found with id: " + id);
                    return new NotFoundException("Appointment not found with id: " + id);
                }
        );
        logger.info("Found appointment: " + appointment.getId());
        return mapToDto(appointment);
    }

    private AppointmentResponseDto mapToDto(Appointment appointment) {
        return objectMapper.convertValue(appointment, AppointmentResponseDto.class);
    }

    private Appointment mapToEntity(AppointmentRequestDto appointmentDto) {
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentDto.getDate());

        Dentist dentist = dentistRepository.findById(appointmentDto.getDentistId()).orElseThrow(
                () -> {
                    logger.error("Dentist not found with id: " + appointment.getDentist().getId());
                    return new NotFoundException("Dentist not found with id: " + appointment.getDentist().getId());
                }
        );

        Patient patient = patientRepository.findById(appointmentDto.getPatientId()).orElseThrow(
                () -> {
                    logger.error("Patient not found with id: " + appointment.getPatient().getId());
                    return new NotFoundException("Patient not found with id: " + appointment.getPatient().getId());
                }
        );

        appointment.setPatient(patient);
        appointment.setDentist(dentist);

        return appointment;
    }
}
