package com.carrotzmarket.api.domain.user.session;

import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.domain.user.controller.UserOpenApiController;
import com.carrotzmarket.api.domain.user.dto.UserLoginRequest;
import com.carrotzmarket.api.domain.user.dto.UserResponse;
import com.carrotzmarket.api.domain.user.dto.UserSessionInfo;
import com.carrotzmarket.api.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class UserOpenApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserOpenApiController userOpenApiController; // Mock 객체를 주입받을 컨트롤러

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userOpenApiController).build(); // MockMvc 초기화
        objectMapper = new ObjectMapper(); // ObjectMapper 수동 초기화
    }

    @Test
    void login_create_session() throws Exception {
        // GIVEN: Mock 데이터 준비
        UserLoginRequest request = new UserLoginRequest("test", "password");
        UserResponse response = new UserResponse();
        response.setLoginId("test");
        response.setEmail("test@gmail.com");

        // Mock의 반환값 설정
        given(userService.login(request)).willReturn(Api.OK(response));

        // WHEN: 로그인 요청 실행
        mockMvc.perform(post("/open-api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("userSession",
                        new UserSessionInfo(null, "test", "test@gmail.com","010-1111-1111", "asdasdasd", "aaaa" ))) // 세션 검증
                .andExpect(jsonPath("$.data.loginId").value("test"));
    }

    @Test
    void session_expiresAfterTimeout() throws Exception {
        // GIVEN: Mock 데이터 준비
        UserLoginRequest request = new UserLoginRequest("testUser", "testPassword");
        UserResponse response = new UserResponse();
        response.setLoginId("testUser");
        response.setEmail("testUser@gmail.com");

        // Mock의 반환값 설정
        given(userService.login(request)).willReturn(Api.OK(response));

        // MockHttpSession 객체 생성
        MockHttpSession session = new MockHttpSession();

        // 로그인 요청으로 세션 생성
        mockMvc.perform(post("/open-api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(session))
                .andExpect(status().isOk());

        // 세션 만료 시뮬레이션
        session.invalidate();

        // 세션 상태 확인 API 호출
        mockMvc.perform(get("/open-api/user/session-status") // session-status 호출
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)) // 만료된 세션 사용
                .andExpect(status().isUnauthorized()); // 401 기대
    }
}
