package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.common.error.UserErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.api.domain.user.controller.model.UserSessionInfo;
import com.carrotzmarket.api.domain.user.controller.model.UserUpdateRequest;
import com.carrotzmarket.api.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private-api/user")
@RequiredArgsConstructor
public class UserPrivateApiController {

    private final UserService userService;

    // 사용자 정보 조회 (인증 필요)
    @GetMapping("/me")
    public Api<UserResponse> getMyInfo(@RequestParam String loginId) {
        UserResponse response = userService.getUserInfo(loginId);
        return Api.OK(response);
    }

    @PutMapping("/update")
    public Api<UserResponse> updateUser(@RequestParam String loginId, @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(loginId, request);
        return Api.OK(response);
    }

    @DeleteMapping("/delete")
    public Api<String> deleteUser(@RequestParam String loginId) {
        userService.deleteUser(loginId);
        return Api.OK("유저의 계정이 삭제되었습니다.");
    }

    @GetMapping("/session-info")
    public Api<UserSessionInfo> getSessionInfo(HttpServletRequest httpRequest) {

        HttpSession session = httpRequest.getSession(false);

        if(session == null || session.getAttribute("userSession") == null) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND, "세션 정보가 없음");
        }

        UserSessionInfo sessionInfo = (UserSessionInfo) session.getAttribute("userSession");
        return Api.OK(sessionInfo);
    }

    @GetMapping("/search")
    public Api<UserResponse> searchUser(@RequestParam String loginId) {
        UserResponse response = userService.findUserByLoginId(loginId);
        return Api.OK(response);
    }

}

