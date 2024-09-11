package com.dh.reservation_system.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDto {
    private Long id;
    private String street;
    private Integer number;
    private String location;
    private String province;
}
