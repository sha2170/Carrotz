package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.domain.user.business.UserBusiness;
import com.carrotzmarket.api.domain.user.controller.model.UserLoginRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.api.domain.user.controller.model.UserSessionInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open-api/user")
@RequiredArgsConstructor
public class UserOpenApiController {

    private final UserBusiness userBusiness;

    // 사용자 등록
    @PostMapping("/register")
    public Api<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        return userBusiness.register(request); // Api<T> 형식으로 응답
    }

    @PostMapping("/login")
    public Api<UserResponse> login(@Valid @RequestBody UserLoginRequest request, HttpServletRequest httpRequest) {

        // 로그인 검증
        Api<UserResponse> response = userBusiness.login(request);

        // 세션 생성 & 정보 저장
        HttpSession session = httpRequest.getSession();
        UserResponse userResponse = response.getData();

        UserSessionInfo sessionInfo = new UserSessionInfo(
                userResponse.getId(),
                userResponse.getLoginId(),
                userResponse.getEmail()
        );
        session.setAttribute("userSession", sessionInfo);

        return response;
    }

    @PostMapping("/logout")
    public Api<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Api.OK("성공적으로 로그아웃 하였습니다.");
    }

    // 세션 상태 확인 API
    @GetMapping("/session-status")
    public Api<String> checkSessionStatus(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false); // 기존 세션 가져오기
        if (session == null || session.getAttribute("userSession") == null) {
            return Api.unauthorized("Session expired or not found"); // 세션 없음
        }
        return Api.OK("Session is active"); // 세션 유효
    }
}

