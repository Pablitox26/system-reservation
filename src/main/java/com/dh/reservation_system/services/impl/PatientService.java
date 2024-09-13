package com.dh.reservation_system.services.impl;

import com.dh.reservation_system.dtos.PatientRequestDto;
import com.dh.reservation_system.dtos.PatientRequestToUpdateDto;
import com.dh.reservation_system.dtos.PatientResponseDto;
import com.dh.reservation_system.entities.Patient;
import com.dh.reservation_system.exceptions.NotFoundException;
import com.dh.reservation_system.repositories.IPatientRepository;
import com.dh.reservation_system.services.IPatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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
    public PatientResponseDto update(PatientRequestToUpdateDto patientRequestToUpdateDto) {
        logger.info("Updating patient: " + patientRequestToUpdateDto.getName());
        findById(patientRequestToUpdateDto.getId());
        Patient updatedPatient = patientRepository.save(mapToEntity(patientRequestToUpdateDto));
        logger.info("Patient updated: "+ updatedPatient.getName());
        return mapToDto(updatedPatient);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting patient by id: "+ id);
        findById(id);
        patientRepository.deleteById(id);
        logger.info("Patient deleted with id: "+ id);
    }

    private PatientResponseDto mapToDto(Patient patient) {
        // Create the DTO and map basic fields
        PatientResponseDto patientResponseDto = new PatientResponseDto();
        patientResponseDto.setId(patient.getId());
        patientResponseDto.setName(patient.getName());
        patientResponseDto.setLastName(patient.getLastName());
        patientResponseDto.setDni(patient.getDni());
        patientResponseDto.setDischargeDate(patient.getDischargeDate().toString()); // Convert LocalDate to String

        // Manually concatenate the address fields
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
