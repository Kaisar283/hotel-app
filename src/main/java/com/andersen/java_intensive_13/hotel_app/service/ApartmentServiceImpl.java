package com.andersen.java_intensive_13.hotel_app.service;

import com.andersen.java_intensive_13.hotel_app.dto.ApartmentDTO;
import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import com.andersen.java_intensive_13.hotel_app.exception.AlreadyReservedException;
import com.andersen.java_intensive_13.hotel_app.exception.NotReservedException;
import com.andersen.java_intensive_13.hotel_app.exception.ResourceNotFoundException;
import com.andersen.java_intensive_13.hotel_app.mapper.ApartmentMapper;
import com.andersen.java_intensive_13.hotel_app.model.Apartment;
import com.andersen.java_intensive_13.hotel_app.model.User;
import com.andersen.java_intensive_13.hotel_app.repository.ApartmentRepository;
import com.andersen.java_intensive_13.hotel_app.repository.UserRepository;
import com.andersen.java_intensive_13.hotel_app.service.service_interface.ApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.andersen.java_intensive_13.hotel_app.util.ApplicationMessage.notFoundById;

@Slf4j
@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;

    private final UserRepository userRepository;

    private final ApartmentMapper apartmentMapper;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository,
                                UserRepository userRepository,
                                ApartmentMapper apartmentMapper) {
        this.apartmentRepository = apartmentRepository;
        this.userRepository = userRepository;
        this.apartmentMapper = apartmentMapper;
    }

    @Transactional
    @Override
    public ApartmentDTO createApartment(ApartmentDTO apartmentDto) {
        if (apartmentDto == null) {
            throw new NullPointerException();
        }
        Apartment apartment = apartmentMapper.toEntity(apartmentDto);
        return apartmentMapper.toDTO(apartmentRepository.save(apartment));

    }

    @Override
    public ApartmentDTO getApartmentById(Integer apartmentId) {
        Optional<Apartment> optionalApartment = apartmentRepository.findById(apartmentId);

        if(optionalApartment.isEmpty()){
            throw new ResourceNotFoundException(notFoundById(apartmentId, Apartment.class.getName()));
        }
        return apartmentMapper.toDTO(optionalApartment.get());
    }

    @Override
    public List<ApartmentDTO> getAllApartments(PageRequest pageRequest) {
        return apartmentRepository.findAll(pageRequest).stream()
                .map(apartment -> apartmentMapper.toDTO(apartment))
                .collect(Collectors.toList());
    }

    public List<ApartmentDTO> getAvailableApartments(PageRequest pageRequest){
        return apartmentRepository.findAllAvailableApartments(pageRequest).stream()
                .map(apartment -> apartmentMapper.toDTO(apartment))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteApartmentById(Integer apartmentId) {
        Optional<Apartment> byId = apartmentRepository.findById(apartmentId);

        if(byId.isEmpty()){
            throw new ResourceNotFoundException(notFoundById(apartmentId, Apartment.class.getName()));
        }
        apartmentRepository.delete(byId.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public ApartmentDTO releaseApartment(Integer apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundById(apartmentId, Apartment.class.getName())));

        if (!apartment.getIsReserved()) {
            throw new NotReservedException("Apartment with id " + apartment.getId() + " not reserved!");
        }

        apartment.setUser(null);
        apartment.setIsReserved(false);

        Apartment updatedApartment = apartmentRepository.save(apartment);
        return apartmentMapper.toDTO(updatedApartment);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApartmentDTO reserveApartment(Integer apartmentID, UserDTO userDTO){
        Apartment apartment = apartmentRepository.findById(apartmentID)
                .orElseThrow(() -> new ResourceNotFoundException(
                        notFoundById(apartmentID, Apartment.class.getName())));

        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        notFoundById(userDTO.getId(), User.class.getName())));

        if (apartment.getIsReserved() && apartment.getUser() != null) {
            throw new AlreadyReservedException("Apartment with id " + apartment.getId() + " already reserved!");
        }

        apartment.setIsReserved(true);
        apartment.setUser(user);

        Apartment savedApartment = apartmentRepository.save(apartment);
        return apartmentMapper.toDTO(savedApartment);
    }
}
