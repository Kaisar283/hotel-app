package com.andersen.java_intensive_13.hotel_app.mapper;

import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import com.andersen.java_intensive_13.hotel_app.enums.UserRole;
import com.andersen.java_intensive_13.hotel_app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMapper {

    public UserDTO toDTO(User user){
        if (user == null){
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userRole(user.getUserRole().toString())
                .build();
    }

    public User toEntity(UserDTO userDTO){
        if (userDTO == null){
            return null;
        }
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .userRole(UserRole.valueOf(userDTO.getUserRole()))
                .build();
    }
}
