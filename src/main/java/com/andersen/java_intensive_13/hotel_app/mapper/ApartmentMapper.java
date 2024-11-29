package com.andersen.java_intensive_13.hotel_app.mapper;

import com.andersen.java_intensive_13.hotel_app.dto.ApartmentDTO;
import com.andersen.java_intensive_13.hotel_app.model.Apartment;
import liquibase.command.CommandOverride;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApartmentMapper {

    UserMapper userMapper;

    public ApartmentMapper() {
        this.userMapper = new UserMapper();
    }

    public ApartmentDTO toDTO(Apartment apartment){

        return ApartmentDTO.builder()
                .id(apartment.getId())
                .price(apartment.getPrice())
                .isReserved(apartment.getIsReserved())
                .userDTO(userMapper.toDTO(apartment.getUser()))
                .build();

    }

    public Apartment toEntity(ApartmentDTO apartmentDTO){

        return Apartment.builder()
                .id(apartmentDTO.getId())
                .price(apartmentDTO.getPrice())
                .isReserved(apartmentDTO.getIsReserved())
                .user(userMapper.toEntity(apartmentDTO.getUserDTO()))
                .build();
    }
}
