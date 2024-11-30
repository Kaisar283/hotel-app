package com.andersen.java_intensive_13.hotel_app.controller;

import com.andersen.java_intensive_13.hotel_app.dto.ApartmentDTO;
import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import com.andersen.java_intensive_13.hotel_app.exception.ResourceNotFoundException;
import com.andersen.java_intensive_13.hotel_app.mapper.ApartmentMapper;
import com.andersen.java_intensive_13.hotel_app.mapper.UserMapper;
import com.andersen.java_intensive_13.hotel_app.service.ApartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.andersen.java_intensive_13.hotel_app.utils.PrepatedEntity.preparedApartmentList;
import static com.andersen.java_intensive_13.hotel_app.utils.PrepatedEntity.preparedUserList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ApartmentControllerTest {

    @Mock
    ApartmentServiceImpl apartmentService;

    @InjectMocks
    ApartmentController apartmentController;

    private final ApartmentMapper apartmentMapper = new ApartmentMapper();
    private final UserMapper userMapper = new UserMapper();


    @Test
    void createNewApartment_ShouldReturnCreatedApartment() {
        ApartmentDTO mockApartment = apartmentMapper.toDTO(preparedApartmentList().get(0));
        when(apartmentService.createApartment(any())).thenReturn(mockApartment);

        ResponseEntity<ApartmentDTO> response = apartmentController.createNewApartment(mockApartment);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockApartment, response.getBody());
        verify(apartmentService, times(1)).createApartment(any());
    }

    @Test
    void getApartmentById_ShouldReturnApartment() {
        ApartmentDTO mockApartment = apartmentMapper.toDTO(preparedApartmentList().get(0));
        when(apartmentService.getApartmentById(1)).thenReturn(mockApartment);

        ResponseEntity<ApartmentDTO> response = apartmentController.getApartmentById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockApartment, response.getBody());
        verify(apartmentService, times(1)).getApartmentById(1);
    }

    @Test
    void getApartmentById_NonExistingApartment_ShouldThrowException() {
        when(apartmentService.getApartmentById(99)).thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () -> apartmentController.getApartmentById(99));
        verify(apartmentService, times(1)).getApartmentById(99);
    }

    @Test
    void getAllApartments_ShouldReturnListOfApartments() {
        List<ApartmentDTO> apartments = preparedApartmentList().stream()
                        .map(apartmentMapper::toDTO).collect(Collectors.toList());
        when(apartmentService.getAllApartments(any())).thenReturn(apartments);

        ResponseEntity<List<ApartmentDTO>> response = apartmentController.getAllApartments(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apartments, response.getBody());
        verify(apartmentService, times(1)).getAllApartments(any());
    }

    @Test
    void getAllApartments_EmptyList_ShouldReturnEmptyResponse() {
        when(apartmentService.getAllApartments(any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<ApartmentDTO>> response = apartmentController.getAllApartments(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(apartmentService, times(1)).getAllApartments(any());
    }

    @Test
    void getAvailableApartments_ShouldReturnAvailableApartments() {
        List<ApartmentDTO> apartments = preparedApartmentList().stream()
                .map(apartmentMapper::toDTO).collect(Collectors.toList());
        when(apartmentService.getAvailableApartments(any())).thenReturn(apartments);

        ResponseEntity<List<ApartmentDTO>> response = apartmentController.getAvailableApartments(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apartments, response.getBody());
        verify(apartmentService, times(1)).getAvailableApartments(any());
    }

    @Test
    void getAvailableApartments_NoAvailableApartments_ShouldReturnEmptyList() {
        when(apartmentService.getAvailableApartments(any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<ApartmentDTO>> response = apartmentController.getAvailableApartments(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(apartmentService, times(1)).getAvailableApartments(any());
    }

    @Test
    void deleteApartmentById_ShouldDeleteApartment() {
        doNothing().when(apartmentService).deleteApartmentById(1);

        ResponseEntity<Void> response = apartmentController.deleteApartmentById(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(apartmentService, times(1)).deleteApartmentById(1);
    }

    @Test
    void deleteApartmentById_NonExistingApartment_ShouldThrowException() {
        doThrow(new ResourceNotFoundException()).when(apartmentService).deleteApartmentById(99);

        assertThrows(ResourceNotFoundException.class, () -> apartmentController.deleteApartmentById(99));
        verify(apartmentService, times(1)).deleteApartmentById(99);
    }

    @Test
    void releaseApartmentById_ShouldReleaseApartment() {
        ApartmentDTO releasedApartment = apartmentMapper.toDTO(preparedApartmentList().get(0));
        when(apartmentService.releaseApartment(1)).thenReturn(releasedApartment);

        ResponseEntity<ApartmentDTO> response = apartmentController.releaseApartmentById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(releasedApartment, response.getBody());
        verify(apartmentService, times(1)).releaseApartment(1);
    }

    @Test
    void releaseApartmentById_NonExistingApartment_ShouldThrowException() {
        when(apartmentService.releaseApartment(99)).thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () -> apartmentController.releaseApartmentById(99));
        verify(apartmentService, times(1)).releaseApartment(99);
    }

    @Test
    void reserveApartment_ShouldReserveApartment() {
        UserDTO user = userMapper.toDTO(preparedUserList().get(0));
        ApartmentDTO reservedApartment = apartmentMapper.toDTO(preparedApartmentList().get(0));
        when(apartmentService.reserveApartment(1, user)).thenReturn(reservedApartment);

        ResponseEntity<ApartmentDTO> response = apartmentController.reserveApartment(1, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservedApartment, response.getBody());
        verify(apartmentService, times(1)).reserveApartment(1, user);
    }

    @Test
    void reserveApartment_NonExistingApartment_ShouldThrowException() {
        UserDTO user = userMapper.toDTO(preparedUserList().get(0));
        when(apartmentService.reserveApartment(99, user)).thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () -> apartmentController.reserveApartment(99, user));
        verify(apartmentService, times(1)).reserveApartment(99, user);
    }
}