package com.dh.reservation_system.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DentistResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String licenseMedical;
}
