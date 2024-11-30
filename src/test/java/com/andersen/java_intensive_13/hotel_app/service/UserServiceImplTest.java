package com.andersen.java_intensive_13.hotel_app.service;

import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import com.andersen.java_intensive_13.hotel_app.exception.ResourceNotFoundException;
import com.andersen.java_intensive_13.hotel_app.mapper.UserMapper;
import com.andersen.java_intensive_13.hotel_app.model.User;
import com.andersen.java_intensive_13.hotel_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.andersen.java_intensive_13.hotel_app.utils.PrepatedEntity.preparedUserList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

class UserServiceImplTest {

    @Mock
    UserMapper userMapper;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    UserMapper mapper = new UserMapper();

    @Test
    void createUser_ShouldReturnCreatedUserDTO_WhenValidUserDTOProvided() {
        User userEntity = preparedUserList().get(1);
        UserDTO userDTO = mapper.toDTO(userEntity);

        when(userMapper.toEntity(userDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void getUserById_ShouldReturnUserDTO_WhenUserExists() {
        Long userId = 1L;
        User userEntity = preparedUserList().get(0);
        UserDTO userDTO = mapper.toDTO(userEntity);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDTOs() {
        List<User> users = preparedUserList();
        List<UserDTO> userDTOs = preparedUserList().stream()
                .map(user -> mapper.toDTO(user)).toList();

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(userRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(users));
        when(userMapper.toDTO(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return UserDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .userRole(String.valueOf(user.getUserRole()))
                    .build();
        });

        List<UserDTO> result = userService.getAllUsers(pageRequest);

        assertEquals(userDTOs.size(), result.size());
        verify(userRepository).findAll(pageRequest);
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersAvailable() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(userRepository.findAll(pageRequest)).thenReturn(Page.empty());

        List<UserDTO> result = userService.getAllUsers(pageRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteUserById_ShouldDeleteUser_WhenUserExists() {
        Long userId = 1L;
        User userEntity = preparedUserList().get(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void deleteUserById_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(userId));
    }
}