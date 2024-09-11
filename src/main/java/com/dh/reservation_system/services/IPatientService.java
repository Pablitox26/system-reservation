package com.dh.reservation_system.services;

import com.dh.reservation_system.dtos.PatientRequestDto;
import com.dh.reservation_system.dtos.PatientRequestToUpdateDto;
import com.dh.reservation_system.dtos.PatientResponseDto;
import com.dh.reservation_system.entities.Patient;

import java.util.List;

public interface IPatientService {
    PatientResponseDto save(PatientRequestDto patientDto);
    List<PatientResponseDto> findAll();
    PatientResponseDto findById(Long id);
    PatientResponseDto update(PatientRequestToUpdateDto patientDto);
    void delete(Long id);
}
