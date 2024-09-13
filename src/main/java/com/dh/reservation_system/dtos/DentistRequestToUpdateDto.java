package com.dh.reservation_system.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DentistRequestToUpdateDto {
    private String name;
    private String lastName;
    private String licenseMedical;
}
