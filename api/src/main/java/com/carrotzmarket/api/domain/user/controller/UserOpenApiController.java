package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.domain.user.business.UserBusiness;
import com.carrotzmarket.api.domain.user.controller.model.UserLoginRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // 사용자 로그인
    @PostMapping("/login")
    public Api<UserResponse> login(@Valid @RequestBody UserLoginRequest request) {
        return userBusiness.login(request); // Api<T> 형식으로 응답
    }
}

