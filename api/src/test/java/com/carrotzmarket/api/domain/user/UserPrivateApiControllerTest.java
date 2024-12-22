package com.carrotzmarket.api.domain.user;

import com.carrotzmarket.api.domain.user.controller.UserPrivateApiController;
import com.carrotzmarket.api.domain.user.dto.UserResponse;
import com.carrotzmarket.api.domain.user.dto.UserUpdateRequest;
import com.carrotzmarket.api.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userResponse = UserResponse.builder()
                .id(1L)
                .loginId("testuser")
                .email("testuser@example.com")
                .phone("010-1234-5678")
                .build();
    }

    @Test
    void getMyInfoTest() throws Exception {
        when(userService.getUserInfo("testuser")).thenReturn(userResponse);

        mockMvc.perform(get("/private-api/user/me")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.loginId").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("testuser@example.com"));

        verify(userService, times(1)).getUserInfo("testuser");
    }

    @Test
    void updateUserTest() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPhone("010-9876-5432");

        when(userService.updateUser(eq("testuser"), any(UserUpdateRequest.class))).thenReturn(userResponse);

        mockMvc.perform(put("/private-api/user/update")
                        .param("loginId", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.data.phone").value("010-1234-5678"));

        verify(userService, times(1)).updateUser(eq("testuser"), any(UserUpdateRequest.class));
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
