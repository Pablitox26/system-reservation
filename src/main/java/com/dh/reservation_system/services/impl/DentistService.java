package com.dh.reservation_system.services.impl;

import com.dh.reservation_system.dtos.DentistRequestDto;
import com.dh.reservation_system.dtos.DentistRequestToUpdateDto;
import com.dh.reservation_system.dtos.DentistResponseDto;
import com.dh.reservation_system.entities.Dentist;
import com.dh.reservation_system.exceptions.NotFoundException;
import com.dh.reservation_system.repositories.IDentistRepository;
import com.dh.reservation_system.services.IDentistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DentistService implements IDentistService {

    private final IDentistRepository dentistRepository;
    private final ObjectMapper objectMapper;

    public DentistService(IDentistRepository dentistRepository, ObjectMapper objectMapper) {
        this.dentistRepository = dentistRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public DentistResponseDto save(DentistRequestDto dentistDto) {
        Dentist dentist = mapToEntity(dentistDto);
        dentist = dentistRepository.save(dentist);
        return mapToDto(dentist);
    }

    @Override
    public List<DentistResponseDto> findAll() {
        return dentistRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public DentistResponseDto findById(Long id) {
        Dentist dentist = dentistRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Dentist not found")
        );
        return mapToDto(dentist);
    }

    @Override
    public DentistResponseDto findByLicenseMedical(String licenseMedical) {
        Dentist dentist =  dentistRepository.findByLicenseMedical(licenseMedical).orElseThrow(
                () -> new NotFoundException("Dentist not found")
        );
        return mapToDto(dentist);
    }

    @Override
    @Transactional
    public DentistResponseDto update(DentistRequestToUpdateDto dentistRequestToUpdateDto) {
        findById(dentistRequestToUpdateDto.getId());
        return mapToDto(dentistRepository.save(mapToEntity(dentistRequestToUpdateDto)));
    }

    @Override
    public void delete(Long id) {
        findById(id);
        dentistRepository.deleteById(id);
    }

    private DentistResponseDto mapToDto(Dentist dentist) {
        return objectMapper.convertValue(dentist, DentistResponseDto.class);
    }

    private Dentist mapToEntity(DentistRequestDto dentistDto) {
        return objectMapper.convertValue(dentistDto, Dentist.class);
    }

    private Dentist mapToEntity(DentistRequestToUpdateDto dentistDto) {
        return objectMapper.convertValue(dentistDto, Dentist.class);
    }
}
