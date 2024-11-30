package com.andersen.java_intensive_13.hotel_app.controller;

import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import com.andersen.java_intensive_13.hotel_app.exception.ResourceNotFoundException;
import com.andersen.java_intensive_13.hotel_app.mapper.UserMapper;
import com.andersen.java_intensive_13.hotel_app.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.andersen.java_intensive_13.hotel_app.utils.PrepatedEntity.preparedUserList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    UserController userController;

    private final UserMapper userMapper = new UserMapper();

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<UserDTO> users = preparedUserList().stream()
                        .map(userMapper::toDTO).collect(Collectors.toList());
        when(userService.getAllUsers(any())).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getAllUsers(any());
    }

    @Test
    void getAllUsers_EmptyList_ShouldReturnEmptyList() {
        when(userService.getAllUsers(any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(userService, times(1)).getAllUsers(any());
    }

    @Test
    void createNewUser_ShouldReturnCreatedUser() {
        UserDTO mockUser = userMapper.toDTO(preparedUserList().get(0));
        when(userService.createUser(any())).thenReturn(mockUser);

        ResponseEntity<UserDTO> response = userController.createNewUser(mockUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).createUser(any());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        UserDTO mockUser = userMapper.toDTO(preparedUserList().get(0));
        when(userService.getUserById(1L)).thenReturn(mockUser);

        ResponseEntity<UserDTO> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_NonExistingUser_ShouldThrowException() {
        when(userService.getUserById(99L)).thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () -> userController.getUserById(99L));
        verify(userService, times(1)).getUserById(99L);
    }

    @Test
    void deleteUserById_ShouldDeleteUser() {
        doNothing().when(userService).deleteUserById(1L);

        ResponseEntity<Void> response = userController.deleteUserById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUserById(1L);
    }

    @Test
    void deleteUserById_NonExistingUser_ShouldThrowException() {
        doThrow(new ResourceNotFoundException()).when(userService).deleteUserById(99L);

        assertThrows(ResourceNotFoundException.class, () -> userController.deleteUserById(99L));
        verify(userService, times(1)).deleteUserById(99L);
    }
}