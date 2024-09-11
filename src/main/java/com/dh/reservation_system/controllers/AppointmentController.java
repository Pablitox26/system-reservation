package com.dh.reservation_system.controllers;

import com.dh.reservation_system.dtos.AppointmentRequestDto;
import com.dh.reservation_system.dtos.AppointmentResponseDto;
import com.dh.reservation_system.services.IAppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final IAppointmentService appointmentService;

    public AppointmentController(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> save(@Valid @RequestBody AppointmentRequestDto appointmentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.save(appointmentDto));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> findAll() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }
}
