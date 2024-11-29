package com.andersen.java_intensive_13.hotel_app.service.service_interface;

import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import com.andersen.java_intensive_13.hotel_app.model.User;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO user);
    UserDTO getUserById(Long userId);
    List<UserDTO> getAllUsers(PageRequest request);
    void deleteUserById(Long userId);
}
