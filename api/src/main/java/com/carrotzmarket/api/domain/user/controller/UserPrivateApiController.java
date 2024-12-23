package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.domain.user.dto.UserResponseDto;
import com.carrotzmarket.api.domain.user.dto.UserSessionInfoDto;
import com.carrotzmarket.api.domain.user.dto.UserUpdateRequestDto;
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
    public ResponseEntity<UserResponseDto> getMyInfo(
            @RequestParam String loginId) {
        UserResponseDto response = userService.getUserInfo(loginId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(
            @RequestParam String loginId) {
        userService.deleteUser(loginId);
        return ResponseEntity.ok("유저의 계정이 삭제되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("성공적으로 로그아웃 하였습니다.");
    }

    @GetMapping("/session-info")
    public ResponseEntity<UserSessionInfoDto> getSessionInfo(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("userSession") == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserSessionInfoDto sessionInfo = (UserSessionInfoDto) session.getAttribute("userSession");
        return ResponseEntity.ok(sessionInfo);
    }

    @GetMapping("/search")
    public ResponseEntity<UserResponseDto> searchUser(@RequestParam String loginId) {
        UserResponseDto response = userService.findUserByLoginId(loginId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/update")
    public ResponseEntity<UserResponseDto> updateUser(
            @RequestParam String loginId,
            @ModelAttribute UserUpdateRequestDto request,
            @RequestParam(required = false) MultipartFile profileImage) {
        UserResponseDto updatedUser = userService.updateUser(loginId, request, profileImage);
        return ResponseEntity.ok(updatedUser);
    }
}
