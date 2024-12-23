package com.carrotzmarket.api.domain.user;

import com.carrotzmarket.api.domain.user.controller.UserPrivateApiController;
import com.carrotzmarket.api.domain.user.dto.UserResponseDto;
import com.carrotzmarket.api.domain.user.dto.UserUpdateRequestDto;
import com.carrotzmarket.api.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserPrivateApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserPrivateApiController userPrivateApiController;

    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userResponseDto = UserResponseDto.builder()
                .id(1L)
                .loginId("testuser")
                .email("testuser@example.com")
                .phone("010-1234-5678")
                .build();
    }

    @Test
    void getMyInfoTest() throws Exception {
        when(userService.getUserInfo("testuser")).thenReturn(userResponseDto);

        mockMvc.perform(get("/private-api/user/me")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.loginId").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("testuser@example.com"));

        verify(userService, times(1)).getUserInfo("testuser");
    }

    @Test
    void testUpdateUser() {
        // Arrange
        UserUpdateRequestDto request = new UserUpdateRequestDto();
        UserResponseDto userResponse = new UserResponseDto();
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);

        when(userService.updateUser(eq("testuser"), any(UserUpdateRequestDto.class), eq(mockFile)))
                .thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponseDto> result = userPrivateApiController.updateUser("testuser", request, mockFile);

        // Assert
        assertEquals(userResponse, result);
        verify(userService, times(1)).updateUser(eq("testuser"), any(UserUpdateRequestDto.class), eq(mockFile));
    }

    @Test
    void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser("testuser");

        mockMvc.perform(delete("/private-api/user/delete")
                        .param("loginId", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("유저의 계정이 삭제되었습니다."));

        verify(userService, times(1)).deleteUser("testuser");
    }
}
