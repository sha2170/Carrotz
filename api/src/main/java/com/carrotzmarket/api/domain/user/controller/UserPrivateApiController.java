package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.common.annotation.UserSession;
import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.common.error.UserErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.business.UserBusiness;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.api.domain.user.controller.model.UserSessionInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/session-info")
    public Api<UserSessionInfo> getSessionInfo(HttpServletRequest httpRequest) {

        // 세션에서 저장된 사용자 정보 가져오기
        HttpSession session = httpRequest.getSession(false);

        // 세션이 저장기록 자체가 없거나 기록은 있지만 비어있는 경우
        if(session == null || session.getAttribute("userSession") == null) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND, "세션 정보가 없음");
        }

        UserSessionInfo sessionInfo = (UserSessionInfo) session.getAttribute("userSession");
        return Api.OK(sessionInfo);
    }
}
