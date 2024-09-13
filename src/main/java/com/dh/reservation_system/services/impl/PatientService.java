package com.dh.reservation_system.services.impl;

import com.dh.reservation_system.dtos.PatientRequestDto;
import com.dh.reservation_system.dtos.PatientRequestToUpdateDto;
import com.dh.reservation_system.dtos.PatientResponseDto;
import com.dh.reservation_system.entities.Address;
import com.dh.reservation_system.entities.Patient;
import com.dh.reservation_system.exceptions.NotFoundException;
import com.dh.reservation_system.repositories.IPatientRepository;
import com.dh.reservation_system.services.IPatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService implements IPatientService {
    private static final Logger logger = Logger.getLogger(PatientService.class);
    private final IPatientRepository patientRepository;
    private final ObjectMapper objectMapper;

    public PatientService(IPatientRepository patientRepository, ObjectMapper objectMapper) {
        this.patientRepository = patientRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public PatientResponseDto save(PatientRequestDto patientDto) {
        logger.info("Saving patient: " + patientDto.getName());
        Patient patient = mapToEntity(patientDto);
        patient = patientRepository.save(patient);
        logger.info("Patient saved: "+ patient.getName());
        return mapToDto(patient);
    }

    @Override
    public List<PatientResponseDto> findAll() {
        logger.info("Finding all patients");
        List<PatientResponseDto> patients = patientRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
        logger.info("Patients found: "+ patients.size());
        return patients;
    }

    @Override
    public PatientResponseDto findById(Long id) {
        logger.info("Finding patient by id: "+ id);
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Patient with id " + id + " not found");
                    return new NotFoundException("Patient with id " + id + " not found");
                }
        );
        logger.info("Patient found with id: "+ id);
        return mapToDto(patient);
    }

    @Override
    @Transactional
    public PatientResponseDto update(Long id, PatientRequestToUpdateDto patientRequestToUpdateDto) {
        logger.info("Updating patient by id: " + id);

        Patient existingPatient = patientRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Patient with id " + id + " not found");
                    return new NotFoundException("Patient with id " + id + " not found");
                }
        );

        if (patientRequestToUpdateDto.getName() != null) {
            existingPatient.setName(patientRequestToUpdateDto.getName());
        }
        if (patientRequestToUpdateDto.getLastName() != null) {
            existingPatient.setLastName(patientRequestToUpdateDto.getLastName());
        }
        if (patientRequestToUpdateDto.getDni() != null) {
            existingPatient.setDni(patientRequestToUpdateDto.getDni());
        }
        if (patientRequestToUpdateDto.getDischargeDate() != null) {
            existingPatient.setDischargeDate(LocalDate.parse(patientRequestToUpdateDto.getDischargeDate()));
        }

        if (patientRequestToUpdateDto.getAddress() != null) {
            Address address = objectMapper.convertValue(patientRequestToUpdateDto.getAddress(), Address.class);
            existingPatient.setAddress(address);
        }

        existingPatient = patientRepository.save(existingPatient);
        logger.info("Patient updated with id: " + id);

        return mapToDto(existingPatient);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting patient by id: "+ id);
        findById(id);
        patientRepository.deleteById(id);
        logger.info("Patient deleted with id: "+ id);
    }

    private PatientResponseDto mapToDto(Patient patient) {
        PatientResponseDto patientResponseDto = new PatientResponseDto();
        patientResponseDto.setId(patient.getId());
        patientResponseDto.setName(patient.getName());
        patientResponseDto.setLastName(patient.getLastName());
        patientResponseDto.setDni(patient.getDni());
        patientResponseDto.setDischargeDate(patient.getDischargeDate().toString()); // Convert LocalDate to String

        if (patient.getAddress() != null) {
            String concatenatedAddress = String.format("%s %d, %s, %s",
                    patient.getAddress().getStreet(),
                    patient.getAddress().getNumber(),
                    patient.getAddress().getLocation(),
                    patient.getAddress().getProvince()
            );
            patientResponseDto.setAddress(concatenatedAddress);
        }

        return patientResponseDto;
    }

    private Patient mapToEntity(PatientRequestDto patientDto) {
        return objectMapper.convertValue(patientDto, Patient.class);
    }

    private Patient mapToEntity(PatientRequestToUpdateDto patientDto) {
        return objectMapper.convertValue(patientDto, Patient.class);
    }
}
