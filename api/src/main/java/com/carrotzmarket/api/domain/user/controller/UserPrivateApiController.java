package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.domain.user.dto.UserResponse;
import com.carrotzmarket.api.domain.user.dto.UserSessionInfo;
import com.carrotzmarket.api.domain.user.dto.UserUpdateRequest;
import com.carrotzmarket.api.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/private-api/user")
@RequiredArgsConstructor
public class UserPrivateApiController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@RequestParam String loginId) {
        UserResponse response = userService.getUserInfo(loginId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String loginId) {
        userService.deleteUser(loginId);
        return ResponseEntity.ok("유저의 계정이 삭제되었습니다.");
    }

    @GetMapping("/session-info")
    public ResponseEntity<UserSessionInfo> getSessionInfo(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("userSession") == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserSessionInfo sessionInfo = (UserSessionInfo) session.getAttribute("userSession");
        return ResponseEntity.ok(sessionInfo);
    }

    @GetMapping("/search")
    public ResponseEntity<UserResponse> searchUser(@RequestParam String loginId) {
        UserResponse response = userService.findUserByLoginId(loginId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/update")
    public ResponseEntity<?> updateUser(
            @RequestParam String loginId,
            @ModelAttribute UserUpdateRequest request,
            @RequestParam(required = false) MultipartFile profileImage
    ) {
        UserResponse updatedUser = userService.updateUser(loginId, request, profileImage);
        return ResponseEntity.ok(updatedUser);
    }


}
