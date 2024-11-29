package com.andersen.java_intensive_13.hotel_app.service;

import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import com.andersen.java_intensive_13.hotel_app.exception.ResourceNotFoundException;
import com.andersen.java_intensive_13.hotel_app.mapper.UserMapper;
import com.andersen.java_intensive_13.hotel_app.model.User;
import com.andersen.java_intensive_13.hotel_app.repository.UserRepository;
import com.andersen.java_intensive_13.hotel_app.service.service_interface.UserService;
import kz.andersen.java_intensive_13.map_db_spring_boot_starter.MapDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.andersen.java_intensive_13.hotel_app.util.ApplicationMessage.notFoundById;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    UserMapper userMapper;

    UserRepository userRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDTO createUser(UserDTO user) {
        User userEntity = userMapper.toEntity(user);
        return userMapper.toDTO(userRepository.save(userEntity));
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        notFoundById(userId, User.class.getName())));
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest).stream()
                .map(user -> userMapper.toDTO(user))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        notFoundById(userId, User.class.getName())));
        userRepository.delete(user);
    }
}
