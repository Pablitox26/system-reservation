package com.dh.reservation_system.services;

import com.dh.reservation_system.entities.Patient;

import java.util.List;

public interface IPatientService {
    Patient save(Patient patient);
    List<Patient> findAll();
    Patient findById(Long id);
    Patient update(Patient patient);
    void delete(Long id);
}
