package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.common.annotation.UserSession;
import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.domain.user.business.UserBusiness;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private-api/user")
@RequiredArgsConstructor
public class UserPrivateApiController {

    private final UserBusiness userBusiness;

    // 사용자 정보 조회 (인증 필요)
    @GetMapping("/me")
    public Api<UserResponse> getMyInfo(@RequestParam Long userId) {
        UserResponse response = userBusiness.getUserInfo(userId);
        return Api.OK(response); // Api<T> 형식으로 응답
    }

    @PostMapping("/add-region")
    public Api<String> addUserRegion(@RequestParam Long userId, @RequestParam Long regionId) {
        userBusiness.addUserRegion(userId, regionId);
        return Api.OK("Region added successfully");
    }
}
