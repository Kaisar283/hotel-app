package com.andersen.java_intensive_13.hotel_app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
@Builder
public class ApartmentDTO {
    private Integer id;
    private Double price;
    private Boolean isReserved;
    private UserDTO userDTO;
}
