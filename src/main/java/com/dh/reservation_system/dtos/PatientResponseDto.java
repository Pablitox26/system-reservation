package com.dh.reservation_system.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String dni;
    private String dischargeDate;
    private String address;
}
