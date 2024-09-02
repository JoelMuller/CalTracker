package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.repository.UserRepository;
import com.jamgm.CalTracker.web.rest.DTO.UserDTO;
import com.jamgm.CalTracker.web.rest.transformer.UserTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserTransformer userTransformer;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    public void beforeEach(){
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1)
                .name("testuser1")
                .email("test1@test.com")
                .password("testPassword")
                .weightLossPerWeek(0.50)
                .build();
        userDTO = UserDTO.builder()
                .id(1)
                .name("testuser2")
                .email("test2@test.com")
                .password("testPassword")
                .weightLossPerWeek(1.0)
                .build();
    }

    @Test
    public void testCreateUser(){
        try (MockedStatic<UserTransformer> mockedTransformer = mockStatic(UserTransformer.class)) {
            mockedTransformer.when(() -> UserTransformer.fromDto(any(UserDTO.class))).thenReturn(user);
            mockedTransformer.when(() -> UserTransformer.toDto(any(User.class))).thenReturn(userDTO);

            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);

            UserDTO result = userService.createUser(userDTO);

            verify(passwordEncoder).encode("testPassword");
            verify(userRepository).save(user);
            assertEquals(userDTO, result);
        }
    }

    @Test
    void testUpdateUser_UserExists_WithPassword() {
        try (MockedStatic<UserTransformer> mockedTransformer = mockStatic(UserTransformer.class)) {
            // Given
            mockedTransformer.when(() -> UserTransformer.fromDto(any(UserDTO.class))).thenReturn(user);
            mockedTransformer.when(() -> UserTransformer.toDto(any(User.class))).thenReturn(userDTO);
            when(userRepository.existsById(anyLong())).thenReturn(true);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);

            // When
            UserDTO result = userService.updateUser(userDTO);

            // Then
            verify(passwordEncoder).encode("testPassword"); // Check that password encoding was called
            verify(userRepository).save(user); // Check that save was called on the repository
            assertEquals(userDTO, result); // Check that the result is as expected
        }
    }

    @Test
    void testUpdateUser_UserExists_WithoutPassword() {
        try (MockedStatic<UserTransformer> mockedTransformer = mockStatic(UserTransformer.class)) {
            // Given
            mockedTransformer.when(() -> UserTransformer.fromDto(any(UserDTO.class))).thenReturn(user);
            mockedTransformer.when(() -> UserTransformer.toDto(any(User.class))).thenReturn(userDTO);
            userDTO.setPassword(""); // Empty password to simulate no password update
            when(userRepository.existsById(anyLong())).thenReturn(true);
            when(userRepository.save(any(User.class))).thenReturn(user);

            // When
            UserDTO result = userService.updateUser(userDTO);

            // Then
            verify(passwordEncoder, never()).encode(anyString()); // Password encoding should not be called
            verify(userRepository).save(user); // Check that save was called on the repository
            assertEquals(userDTO, result); // Check that the result is as expected
        }
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        // Given
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(userDTO);
        });

        assertEquals("User with id: 1 does not exist", exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // Save should never be called
    }

    @Test
    void testGetUserById_UserExists() {
        try (MockedStatic<UserTransformer> mockedTransformer = mockStatic(UserTransformer.class)) {
            // Given
            when(userRepository.existsById(anyLong())).thenReturn(true);
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            mockedTransformer.when(() -> UserTransformer.toDto(any(User.class))).thenReturn(userDTO);

            // When
            UserDTO result = userService.getUserById(1L);

            // Then
            verify(userRepository).findById(1L);
            assertEquals(userDTO, result);
        }
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        // Given
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User with id: 1 does not exist", exception.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void testDeleteUser_UserExists() {
        // Given
        when(userRepository.existsById(anyLong())).thenReturn(true);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserDoesNotExist() {
        // Given
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User with id: 1 does not exist", exception.getMessage());
        verify(userRepository, never()).deleteById(anyLong());
    }
}
