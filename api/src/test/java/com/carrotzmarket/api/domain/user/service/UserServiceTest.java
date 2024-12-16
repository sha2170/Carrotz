package com.carrotzmarket.api.domain.user.service;

import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.db.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 초기화
    }

    @Test
    public void testRegister_Success() {
        // Given
        UserEntity newUser = new UserEntity();
        newUser.setLoginid("testuser");
        newUser.setPassword("password");
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.empty());

        // When
        userService.register(newUser);

        // Then
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    public void testRegister_UserAlreadyExists() {
        // Given
        UserEntity existingUser = new UserEntity();
        existingUser.setLoginid("testuser");
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.of(existingUser));

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> userService.register(existingUser));
        assertEquals("이미 존재하는 로그인 ID입니다.", exception.getMessage());
    }

    @Test
    public void testLogin_Success() {
        // Given
        UserEntity user = new UserEntity();
        user.setLoginid("testuser");
        user.setPassword("password");
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.of(user));

        // When
        UserEntity result = userService.login("testuser", "password");

        // Then
        assertEquals(user, result);
    }

    @Test
    public void testLogin_InvalidCredentials() {
        // Given
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> userService.login("testuser", "wrongpassword"));
        assertEquals("로그인 정보가 잘못되었습니다.", exception.getMessage());
    }

    @Test
    public void testFindById_Success() {
        // Given
        UserEntity user = new UserEntity();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        UserEntity result = userService.findById(1L);

        // Then
        assertEquals(user, result);
    }

    @Test
    public void testFindById_UserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> userService.findById(1L));
        assertEquals("해당 ID의 사용자를 찾을 수 없습니다.", exception.getMessage());
    }
}

