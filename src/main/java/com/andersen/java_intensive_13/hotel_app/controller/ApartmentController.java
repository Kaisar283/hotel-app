package com.andersen.java_intensive_13.hotel_app.controller;

import com.andersen.java_intensive_13.hotel_app.dto.ApartmentDTO;
import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import com.andersen.java_intensive_13.hotel_app.service.ApartmentServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apartments")
public class ApartmentController {

    private final ApartmentServiceImpl apartmentService;

    private final String DEFAULT_PAGE = "0";

    private final String DEFAULT_PAGE_SIZE = "10";

    public ApartmentController(ApartmentServiceImpl apartmentService) {
        this.apartmentService = apartmentService;
    }

    @PostMapping
    public ResponseEntity<ApartmentDTO> createNewApartment(@RequestBody ApartmentDTO apartment) {
        ApartmentDTO createdApartment = apartmentService.createApartment(apartment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApartment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentDTO> getApartmentById(@PathVariable Integer id) {
        ApartmentDTO apartment = apartmentService.getApartmentById(id);
        return ResponseEntity.ok(apartment);
    }

    @GetMapping
    public ResponseEntity<List<ApartmentDTO>> getAllApartments(
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE, required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) Integer pageSize
    ) {
        List<ApartmentDTO> apartments = apartmentService.getAllApartments(PageRequest.of(page, pageSize));
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ApartmentDTO>> getAvailableApartments(
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE, required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) Integer pageSize
    ) {
        List<ApartmentDTO> apartments = apartmentService
                .getAvailableApartments(PageRequest.of(page, pageSize));
        return ResponseEntity.ok(apartments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartmentById(@PathVariable Integer id) {
        apartmentService.deleteApartmentById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<ApartmentDTO> releaseApartmentById(@PathVariable Integer id) {
        ApartmentDTO apartmentDTO = apartmentService.releaseApartment(id);
        return ResponseEntity.ok(apartmentDTO);
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ApartmentDTO> reserveApartment(
            @PathVariable Integer id,
            @RequestBody UserDTO userDTO) {
        ApartmentDTO reservedApartment = apartmentService.reserveApartment(id, userDTO);
        return ResponseEntity.ok(reservedApartment);
    }
}

