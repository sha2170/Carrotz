package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.domain.user.dto.UserLoginRequest;
import com.carrotzmarket.api.domain.user.dto.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.dto.UserResponse;
import com.carrotzmarket.api.domain.user.dto.UserSessionInfo;
import com.carrotzmarket.api.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/open-api/user")
@RequiredArgsConstructor
public class UserOpenApiController {

    private final UserService userService;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<UserResponse> register(
            @RequestPart @Valid UserRegisterRequest request,
            @RequestPart(value = "file", required = false) MultipartFile profileImage) {

        UserResponse response = userService.register(request, profileImage);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserLoginRequest request, HttpServletRequest httpRequest) {
        UserResponse response = userService.login(request);

        HttpSession session = httpRequest.getSession();
        session.setAttribute("userSession", new UserSessionInfo(
                response.getId(),
                response.getLoginId(),
                response.getEmail(),
                response.getPhone(),
                response.getProfileImageUrl(),
                response.getRegion()
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("성공적으로 로그아웃 하였습니다.");
    }

    @GetMapping("/session-status")
    public ResponseEntity<String> checkSessionStatus(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("userSession") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired or not found");
        }
        return ResponseEntity.ok("세션 유지중");
    }
}