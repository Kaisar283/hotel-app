package com.andersen.java_intensive_13.hotel_app.service.service_interface;

import com.andersen.java_intensive_13.hotel_app.dto.ApartmentDTO;
import com.andersen.java_intensive_13.hotel_app.model.Apartment;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.List;

public interface ApartmentService {

    ApartmentDTO createApartment(ApartmentDTO apartment);
    ApartmentDTO getApartmentById(Integer apartmentId);
    List<ApartmentDTO> getAllApartments(PageRequest pageRequest);
    void deleteApartmentById(Integer apartmentId);
}
