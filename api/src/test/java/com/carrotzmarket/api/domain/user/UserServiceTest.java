package com.carrotzmarket.api.domain.user;

import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.common.error.ErrorCodeInterface;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.business.UserBusiness;
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

public class UserServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserBusiness userBusiness;

    private UserRegisterRequest registerRequest;
    private UserEntity userEntity;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerRequest = new UserRegisterRequest("testuser", "password", "test@example.com", "010-1234-5678", "1990-01-01", null);
        userEntity = UserEntity.builder()
                .loginid("testuser")
                .password("password")
                .email("test@example.com")
                .build();
        userResponse = UserResponse.builder()
                .loginId("testuser")
                .email("test@example.com")
                .build();
    }

    @Test
    void 사용자_등록_성공() {
        when(userConverter.toEntity(registerRequest)).thenReturn(userEntity);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        Api<UserResponse> response = userBusiness.register(registerRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getData().getLoginId());
        verify(userService, times(1)).register(userEntity);
    }

    @Test
    void 사용자_등록_실패_중복된_ID() {
        // Given
        ErrorCodeInterface mockErrorCode = mock(ErrorCodeInterface.class);
        when(mockErrorCode.getDescription()).thenReturn("이미 존재하는 로그인 ID");

        // `userConverter.toEntity`가 null이 아닌 객체를 반환하도록 Mock 설정
        when(userConverter.toEntity(registerRequest)).thenReturn(userEntity);

        doThrow(new ApiException(mockErrorCode))
                .when(userService).register(any(UserEntity.class));

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> userBusiness.register(registerRequest));
        assertEquals("이미 존재하는 로그인 ID", exception.getMessage());
    }


    @Test
    void 사용자_로그인_성공() {
        UserLoginRequest loginRequest = new UserLoginRequest("testuser", "password");
        when(userService.login("testuser", "password")).thenReturn(userEntity);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        Api<UserResponse> response = userBusiness.login(loginRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getData().getLoginId());
    }

    @Test
    void 사용자_정보_조회_성공() {
        when(userService.findById(1L)).thenReturn(userEntity);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        UserResponse response = userBusiness.getUserInfo(1L);

        assertNotNull(response);
        assertEquals("testuser", response.getLoginId());
    }
}
