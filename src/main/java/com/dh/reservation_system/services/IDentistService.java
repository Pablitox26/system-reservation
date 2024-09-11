package com.dh.reservation_system.services;

import com.dh.reservation_system.dtos.DentistRequestDto;
import com.dh.reservation_system.dtos.DentistRequestToUpdateDto;
import com.dh.reservation_system.dtos.DentistResponseDto;

import java.util.List;

public interface IDentistService {
    DentistResponseDto save(DentistRequestDto dentistDto);
    List<DentistResponseDto> findAll();
    DentistResponseDto findById(Long id);
    DentistResponseDto update(DentistRequestToUpdateDto dentistDto);
    DentistResponseDto findByLicenseMedical(String licenseMedical);
    void delete(Long id);
}
