package com.dh.reservation_system.cntrollers;

import com.dh.reservation_system.dtos.PatientRequestDto;
import com.dh.reservation_system.dtos.PatientRequestToUpdateDto;
import com.dh.reservation_system.dtos.PatientResponseDto;
import com.dh.reservation_system.services.IPatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final IPatientService patientService;

    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> save(@Valid @RequestBody PatientRequestDto patientDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.save(patientDto));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> findAll() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @PutMapping
    public ResponseEntity<PatientResponseDto> update(@Valid @RequestBody PatientRequestToUpdateDto patientRequestToUpdateDto) {
        return ResponseEntity.ok(patientService.update(patientRequestToUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
