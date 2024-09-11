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
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DentistService implements IDentistService {
    private static final Logger logger = Logger.getLogger(DentistService.class);
    private final IDentistRepository dentistRepository;
    private final ObjectMapper objectMapper;

    public DentistService(IDentistRepository dentistRepository, ObjectMapper objectMapper) {
        this.dentistRepository = dentistRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public DentistResponseDto save(DentistRequestDto dentistDto) {
        logger.info("Saving dentist: " + dentistDto.getName());
        Dentist dentist = mapToEntity(dentistDto);
        dentist = dentistRepository.save(dentist);
        logger.info("Dentist saved: "+ dentist.getName());
        return mapToDto(dentist);
    }

    @Override
    public List<DentistResponseDto> findAll() {
        logger.info("Finding all dentists");
        List<DentistResponseDto> dentists = dentistRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
        logger.info("Dentists found: "+ dentists.size());
        return dentists;
    }

    @Override
    public DentistResponseDto findById(Long id) {
        logger.info("Finding dentist by id: "+ id);
        Dentist dentist = dentistRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Dentist with id " + id + " not found");
                    return new NotFoundException("Dentist with id " + id + " not found");
                }
        );
        logger.info("Dentist found with id: "+ id);
        return mapToDto(dentist);
    }

    @Override
    public DentistResponseDto findByLicenseMedical(String licenseMedical) {
        logger.info("Finding dentist by license medical: "+ licenseMedical);
        Dentist dentist = dentistRepository.findByLicenseMedical(licenseMedical).orElseThrow(
                () -> {
                    logger.error("Dentist with license medical {} not found"+ licenseMedical);
                    return new NotFoundException("Dentist with license medical " + licenseMedical + " not found");
                }
        );
        logger.info("Dentist found with license medical: "+ licenseMedical);
        return mapToDto(dentist);
    }

    @Override
    @Transactional
    public DentistResponseDto update(DentistRequestToUpdateDto dentistRequestToUpdateDto) {
        logger.info("Updateing dentist with id: "+ dentistRequestToUpdateDto.getId());
        findById(dentistRequestToUpdateDto.getId());
        Dentist updatedDentist = dentistRepository.save(mapToEntity(dentistRequestToUpdateDto));
        logger.info("Dentist with ID: " + dentistRequestToUpdateDto.getId() + " updated successfully");
        return mapToDto(updatedDentist);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting dentist with id: " + id);
        findById(id);
        dentistRepository.deleteById(id);
        logger.info("Dentist with ID: " + id +" deleted successfully");
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
