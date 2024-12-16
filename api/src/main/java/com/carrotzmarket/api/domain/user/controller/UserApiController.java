package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.common.annotation.UserSession;
import com.carrotzmarket.api.domain.user.business.UserBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserBusiness userBusiness;

    @GetMapping("/me")
    public Api<UserResponse> me(@UserSession User user){
        var response = userBusiness.me(user.getId());
        return Api.OK();
    }
}
