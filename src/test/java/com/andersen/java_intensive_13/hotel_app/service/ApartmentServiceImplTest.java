package com.andersen.java_intensive_13.hotel_app.service;

import com.andersen.java_intensive_13.hotel_app.dto.ApartmentDTO;
import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import com.andersen.java_intensive_13.hotel_app.exception.AlreadyReservedException;
import com.andersen.java_intensive_13.hotel_app.exception.NotReservedException;
import com.andersen.java_intensive_13.hotel_app.exception.ResourceNotFoundException;
import com.andersen.java_intensive_13.hotel_app.mapper.ApartmentMapper;
import com.andersen.java_intensive_13.hotel_app.mapper.UserMapper;
import com.andersen.java_intensive_13.hotel_app.model.Apartment;
import com.andersen.java_intensive_13.hotel_app.model.User;
import com.andersen.java_intensive_13.hotel_app.repository.ApartmentRepository;
import com.andersen.java_intensive_13.hotel_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.andersen.java_intensive_13.hotel_app.utils.PrepatedEntity.preparedApartmentList;
import static com.andersen.java_intensive_13.hotel_app.utils.PrepatedEntity.preparedUserList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ApartmentServiceImplTest {

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private ApartmentMapper apartmentMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApartmentServiceImpl apartmentService;

    @Test
    void createApartment_ShouldReturnCreatedApartmentDTO_WhenValidApartmentDTOProvided() {
        Apartment apartment = preparedApartmentList().get(0);
        ApartmentMapper mapper = new ApartmentMapper();
        ApartmentDTO apartmentDTO = mapper.toDTO(apartment);

        when(apartmentMapper.toEntity(apartmentDTO)).thenReturn(apartment);
        when(apartmentRepository.save(apartment)).thenReturn(apartment);
        when(apartmentMapper.toDTO(apartment)).thenReturn(apartmentDTO);

        ApartmentDTO result = apartmentService.createApartment(apartmentDTO);

        assertNotNull(result);
        assertEquals(apartmentDTO, result);
        verify(apartmentRepository, times(1)).save(apartment);
        verify(apartmentMapper).toDTO(apartment);
        verify(apartmentMapper).toEntity(apartmentDTO);
    }

    @Test
    void createApartment_ShouldThrowNullPointerException_WhenNullApartmentDTOProvided() {
        assertThrows(NullPointerException.class, () -> apartmentService.createApartment(null));
    }

    @Test
    void getApartmentById_ShouldReturnApartmentDTO_WhenApartmentExists() {
        ApartmentMapper mapper = new ApartmentMapper();
        Apartment apartment = preparedApartmentList().get(0);
        ApartmentDTO apartmentDTO = mapper.toDTO(apartment);

        when(apartmentRepository.findById(apartment.getId())).thenReturn(Optional.of(apartment));
        when(apartmentMapper.toDTO(apartment)).thenReturn(apartmentDTO);

        ApartmentDTO result = apartmentService.getApartmentById(apartment.getId());

        assertNotNull(result);
        assertEquals(apartmentDTO, result);
        verify(apartmentRepository).findById(apartment.getId());
        verify(apartmentMapper).toDTO(apartment);
    }

    @Test
    void getApartmentById_ShouldThrowResourceNotFoundException_WhenApartmentNotFound() {
        Integer apartmentId = 999;

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> apartmentService.getApartmentById(apartmentId));
        verify(apartmentRepository).findById(apartmentId);
    }

    @Test
    void getAllApartments_ShouldReturnListOfApartmentDTOs() {
        ApartmentMapper mapper = new ApartmentMapper();
        List<Apartment> apartments = preparedApartmentList();
        List<ApartmentDTO> apartmentDTOs = preparedApartmentList().stream()
                .map(mapper::toDTO).toList();

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(apartmentRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(apartments));
        when(apartmentMapper.toDTO(any(Apartment.class))).thenAnswer(invocation -> {
            Apartment apt = invocation.getArgument(0);
            return ApartmentDTO.builder()
                    .id(apt.getId())
                    .price(apt.getPrice())
                    .userDTO(null)
                    .isReserved(apt.getIsReserved())
                    .build();
        });

        List<ApartmentDTO> result = apartmentService.getAllApartments(pageRequest);

        assertEquals(apartmentDTOs.size(), result.size());
        verify(apartmentRepository).findAll(pageRequest);
    }

    @Test
    void getAllApartments_ShouldReturnEmptyList_WhenNoApartmentsAvailable() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(apartmentRepository.findAll(pageRequest)).thenReturn(Page.empty());

        List<ApartmentDTO> result = apartmentService.getAllApartments(pageRequest);

        assertTrue(result.isEmpty());
        verify(apartmentRepository).findAll(pageRequest);
    }

    @Test
    void getAvailableApartments_ShouldReturnListOfAvailableApartmentDTOs() {
        ApartmentMapper mapper = new ApartmentMapper();
        List<Apartment> availableApartments = preparedApartmentList();
        List<ApartmentDTO> availableApartmentDTOs = preparedApartmentList().stream()
                .map(mapper::toDTO).toList();

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(apartmentRepository.findAllAvailableApartments(pageRequest)).thenReturn(availableApartments);
        when(apartmentMapper.toDTO(any(Apartment.class))).thenAnswer(invocation -> {
            Apartment apt = invocation.getArgument(0);
            return ApartmentDTO.builder()
                    .id(apt.getId())
                    .price(apt.getPrice())
                    .userDTO(null)
                    .isReserved(apt.getIsReserved())
                    .build();
        });

        List<ApartmentDTO> result = apartmentService.getAvailableApartments(pageRequest);

        assertEquals(availableApartmentDTOs.size(), result.size());
    }

    @Test
    void getAvailableApartments_ShouldReturnEmptyList_WhenNoAvailableApartmentsFound() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(apartmentRepository.findAllAvailableApartments(pageRequest)).thenReturn(Collections.emptyList());

        List<ApartmentDTO> result = apartmentService.getAvailableApartments(pageRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteApartmentById_ShouldDeleteApartment_WhenApartmentExists() {
        Integer apartmentId = 1;
        Apartment apartment = preparedApartmentList().get(apartmentId);

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(apartment));

        apartmentService.deleteApartmentById(apartmentId);

        verify(apartmentRepository, times(1)).delete(apartment);
    }

    @Test
    void deleteApartmentById_ShouldThrowResourceNotFoundException_WhenApartmentNotFound() {
        Integer apartmentId = 999;

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> apartmentService.deleteApartmentById(apartmentId));
    }

    @Test
    void releaseApartment_ShouldUpdateApartment_WhenApartmentIsReserved() {
        User alice = preparedUserList().get(0);
        Integer apartmentId = 1;
        Apartment apartment = preparedApartmentList().get(0);
        apartment.setIsReserved(true);
        apartment.setUser(alice);

        Apartment releasedApartment = preparedApartmentList().get(0);
        ApartmentMapper mapper = new ApartmentMapper();
        ApartmentDTO apartmentDTO = mapper.toDTO(releasedApartment);

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(apartment));
        when(apartmentRepository.save(any(Apartment.class))).thenReturn(releasedApartment);
        when(apartmentMapper.toDTO(releasedApartment)).thenReturn(apartmentDTO);

        ApartmentDTO result = apartmentService.releaseApartment(apartmentId);

        assertNotNull(result);
        assertEquals(apartmentDTO, result);
        verify(apartmentRepository).findById(apartmentId);
        verify(apartmentRepository).save(any(Apartment.class));
        verify(apartmentMapper).toDTO(releasedApartment);
    }

    @Test
    void releaseApartment_ShouldThrowNotReservedException_WhenApartmentIsNotReserved() {
        Integer apartmentId = 1;
        Apartment apartment = preparedApartmentList().get(apartmentId);

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(apartment));

        assertThrows(NotReservedException.class, () -> apartmentService.releaseApartment(apartmentId));
    }

    @Test
    void reserveApartment_ShouldUpdateApartment_WhenApartmentIsAvailable() {
        ApartmentMapper mapper = new ApartmentMapper();
        UserMapper userMapper = new UserMapper();

        Integer apartmentId = 1;
        User user = preparedUserList().get(0);
        UserDTO userDTO = userMapper.toDTO(user);

        Apartment apartment = preparedApartmentList().get(0);
        Apartment reservedApartment = preparedApartmentList().get(0);
        reservedApartment.setUser(user);
        reservedApartment.setIsReserved(true);

        ApartmentDTO apartmentDTO = mapper.toDTO(reservedApartment);

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(apartment));
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));
        when(apartmentRepository.save(any(Apartment.class))).thenReturn(reservedApartment);
        when(apartmentMapper.toDTO(reservedApartment)).thenReturn(apartmentDTO);

        ApartmentDTO result = apartmentService.reserveApartment(apartmentId, userDTO);

        assertNotNull(result);
        assertEquals(apartmentDTO, result);
        verify(apartmentRepository).findById(apartmentId);
        verify(userRepository).findById(userDTO.getId());
        verify(apartmentRepository).save(any(Apartment.class));
        verify(apartmentMapper).toDTO(reservedApartment);
    }

    @Test
    void reserveApartment_ShouldThrowAlreadyReservedException_WhenApartmentAlreadyReserved() {
        UserMapper mapper = new UserMapper();
        Integer apartmentId = 1;
        Apartment apartment = preparedApartmentList().get(1);
        User user = preparedUserList().get(1);
        apartment.setUser(user);
        apartment.setIsReserved(true);
        UserDTO userDTO = mapper.toDTO(user);

        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(apartment));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(AlreadyReservedException.class, () -> apartmentService.reserveApartment(apartmentId, userDTO));
    }
}