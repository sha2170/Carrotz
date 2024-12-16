package com.carrotzmarket.api.domain.user.business;

import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.common.error.UserErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.controller.model.UserLoginRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.api.domain.user.converter.UserConverter;
import com.carrotzmarket.api.domain.user.service.UserService;
import com.carrotzmarket.db.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserBusinessTest {

    @Mock
    private UserService userService;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserBusiness userBusiness;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    public void testRegister_Success() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest(
                "testuser", "password", "test@example.com", "010-1234-5678", "1990-01-01", null
        );
        UserEntity userEntity = new UserEntity();
        UserResponse userResponse = new UserResponse();

        when(userConverter.toEntity(request)).thenReturn(userEntity);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        // When
        Api<UserResponse> response = userBusiness.register(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getResult()); // 응답 결과 확인
        assertEquals("OK", response.getResult().getCode()); // 성공 코드 확인
        assertNotNull(response.getData()); // 데이터가 반환되었는지 확인
        verify(userService, times(1)).register(userEntity);
        verify(userConverter, times(1)).toEntity(request);
        verify(userConverter, times(1)).toResponse(userEntity);
    }

    @Test
    public void testLogin_Success() {
        // Given
        UserLoginRequest request = new UserLoginRequest("testuser", "password");
        UserEntity userEntity = new UserEntity();
        UserResponse userResponse = new UserResponse();

        when(userService.login("testuser", "password")).thenReturn(userEntity);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        // When
        Api<UserResponse> response = userBusiness.login(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getResult());
        assertEquals("OK", response.getResult().getCode());
        assertNotNull(response.getData());
        verify(userService, times(1)).login("testuser", "password");
        verify(userConverter, times(1)).toResponse(userEntity);
    }

    @Test
    public void testLogin_UserNotFound() {
        // Given
        UserLoginRequest request = new UserLoginRequest("testuser", "wrongpassword");
        when(userService.login("testuser", "wrongpassword")).thenThrow(new ApiException(UserErrorCode.USER_NOT_FOUND));

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> userBusiness.login(request));
        assertEquals("USER_NOT_FOUND", exception.getMessage());
        verify(userService, times(1)).login("testuser", "wrongpassword");
    }

    @Test
    public void testGetUserInfo_Success() {
        // Given
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        UserResponse userResponse = new UserResponse();

        when(userService.findById(userId)).thenReturn(userEntity);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        // When
        UserResponse response = userBusiness.getUserInfo(userId);

        // Then
        assertNotNull(response);
        verify(userService, times(1)).findById(userId);
        verify(userConverter, times(1)).toResponse(userEntity);
    }

    @Test
    public void testGetUserInfo_UserNotFound() {
        // Given
        Long userId = 999L;
        when(userService.findById(userId)).thenThrow(new ApiException(UserErrorCode.USER_NOT_FOUND));

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> userBusiness.getUserInfo(userId));
        assertEquals("USER_NOT_FOUND", exception.getMessage());
        verify(userService, times(1)).findById(userId);
    }
}
