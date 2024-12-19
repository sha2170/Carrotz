package com.carrotzmarket.api.domain.user;

import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.common.error.ErrorCodeInterface;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.controller.model.UserLoginRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.api.domain.user.converter.UserConverter;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.api.domain.user.service.UserService;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserService userService; // UserService를 중심으로 테스트

    private UserRegisterRequest registerRequest;
    private UserEntity userEntity;
    private UserResponse userResponse;
    private RegionEntity regionEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 테스트 데이터 초기화
        regionEntity = RegionEntity.builder()
                .id(1L)
                .name("Test Region")
                .build();

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
                .build();

        userResponse = UserResponse.builder()
                .loginId("testuser")
                .email("test@example.com")
                .regionName("Test Region")
                .build();
    }

    @Test
    void 사용자_등록_성공() {
        // Mock 설정
        when(userConverter.toEntity(registerRequest)).thenReturn(userEntity);
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.empty());
        doNothing().when(userRepository).save(userEntity);

        // 테스트 실행
        Api<UserResponse> response = userService.register(registerRequest);

        // 검증
        assertNotNull(response);
        assertEquals("testuser", response.getData().getLoginId());
        assertEquals("Test Region", response.getData().getRegionName());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void 사용자_등록_실패_중복된_ID() {
        // Mock 설정
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.of(userEntity));

        // 테스트 실행 및 검증
        ApiException exception = assertThrows(ApiException.class, () -> userService.register(registerRequest));
        assertEquals("이미 존재하는 로그인 아이디 입니다.", exception.getMessage());
    }

    @Test
    void 사용자_로그인_성공() {
        // Mock 설정
        UserLoginRequest loginRequest = new UserLoginRequest("testuser", "password");
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.of(userEntity));
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        // 테스트 실행
        Api<UserResponse> response = userService.login(loginRequest);

        // 검증
        assertNotNull(response);
        assertEquals("testuser", response.getData().getLoginId());
    }

    @Test
    void 사용자_정보_조회_성공() {
        // Mock 설정
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.of(userEntity));
        when(userConverter.toResponse(userEntity)).thenReturn(userResponse);

        // 테스트 실행
        UserResponse response = userService.getUserInfo("testuser");

        // 검증
        assertNotNull(response);
        assertEquals("testuser", response.getLoginId());
    }

    @Test
    void 사용자_정보_조회_실패() {
        // Mock 설정
        when(userRepository.findByLoginId("nonexistentuser")).thenReturn(Optional.empty());

        // 테스트 실행 및 검증
        ApiException exception = assertThrows(ApiException.class, () -> userService.getUserInfo("nonexistentuser"));
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }
}
