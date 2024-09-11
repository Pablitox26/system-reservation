package com.dh.reservation_system.cntrollers;

import com.dh.reservation_system.dtos.DentistRequestDto;
import com.dh.reservation_system.dtos.DentistRequestToUpdateDto;
import com.dh.reservation_system.dtos.DentistResponseDto;
import com.dh.reservation_system.services.IDentistService;
import com.dh.reservation_system.services.impl.DentistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dentists")
public class DentistController {

    private final IDentistService dentistService;

    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @PostMapping
    public ResponseEntity<DentistResponseDto> save(@Valid @RequestBody DentistRequestDto dentistRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dentistService.save(dentistRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<DentistResponseDto>> findAll() {
        return ResponseEntity.ok(dentistService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(dentistService.findById(id));
    }

    @GetMapping("/licenseMedical/{licenseMedical}")
    public ResponseEntity<DentistResponseDto> findByLicenseMedical(@PathVariable String licenseMedical) {
        return ResponseEntity.ok(dentistService.findByLicenseMedical(licenseMedical));
    }

    @PutMapping
    public ResponseEntity<DentistResponseDto> update(@Valid @RequestBody DentistRequestToUpdateDto dentistRequestDto) {
        return ResponseEntity.ok(dentistService.update(dentistRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dentistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
