package com.dh.reservation_system.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDto {
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "Number is required")
    private Integer number;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Province is required")
    private String province;
}
