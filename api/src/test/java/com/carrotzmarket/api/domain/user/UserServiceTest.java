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
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import com.carrotzmarket.db.user.UserRegionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    private RegionEntity regionEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a mock region entity
        regionEntity = RegionEntity.builder()
                .id(1L)
                .name("Test Region")
                .build();

        // Create a mock user entity
        registerRequest = new UserRegisterRequest(
                "testuser",
                "password",
                "test@example.com",
                "010-1234-5678",
                LocalDate.of(1990, 1, 1),
                null,  // profileImageUrl
                1L     // regionId
        );

        userEntity = UserEntity.builder()
                .loginid("testuser")
                .password("password")
                .email("test@example.com")
                .userRegions(new ArrayList<>())
                .build();

        // Create a mock user-region entity
        UserRegionEntity userRegionEntity = UserRegionEntity.builder()
                .user(userEntity)
                .region(regionEntity)
                .build();

        // Add the userRegionEntity to the userEntity's regions
        userEntity.getUserRegions().add(userRegionEntity);

        // Create a mock user response
        userResponse = UserResponse.builder()
                .loginId("testuser")
                .email("test@example.com")
                .regionName("Test Region") // Include region name in the response
                .build();
    }


    @Test
    void 사용자_등록_성공() {
        when(userConverter.toEntity(registerRequest)).thenReturn(userEntity);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        doNothing().when(userService).register(userEntity);

        Api<UserResponse> response = userBusiness.register(registerRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getData().getLoginId());
        assertEquals("Test Region", response.getData().getRegionName());
        verify(userService, times(1)).register(userEntity);
    }

    @Test
    void 사용자_등록_실패_중복된_ID() {
        ErrorCodeInterface mockErrorCode = mock(ErrorCodeInterface.class);
        when(mockErrorCode.getDescription()).thenReturn("이미 존재하는 로그인 ID");

        when(userConverter.toEntity(registerRequest)).thenReturn(userEntity);

        doThrow(new ApiException(mockErrorCode))
                .when(userService).register(any(UserEntity.class));

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
        assertEquals("Test Region", response.getData().getRegionName());
    }

    @Test
    void 사용자_정보_조회_성공() {
        when(userService.findById(1L)).thenReturn(userEntity);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        UserResponse response = userBusiness.getUserInfo(1L);

        assertNotNull(response);
        assertEquals("testuser", response.getLoginId());
        assertEquals("Test Region", response.getRegionName());
    }
}
