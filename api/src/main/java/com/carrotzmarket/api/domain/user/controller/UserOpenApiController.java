package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.domain.user.business.UserBusiness;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-api/user")
@RequiredArgsConstructor
public class UserOpenApiController {

    private final UserBusiness userBusiness;

    public Api<UserResponse> register(@Valid @RequestBody Api<userRegisterRequest> request) {
        var response = userbusiness.register(request.getBody());
        return Api.OK(response);
    }
}
