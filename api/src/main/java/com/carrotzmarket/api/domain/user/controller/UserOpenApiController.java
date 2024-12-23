package com.carrotzmarket.api.domain.user.controller;

import com.carrotzmarket.api.domain.user.dto.*;
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
    public ResponseEntity<UserResponseDto> register(
            @ModelAttribute @Valid UserRegisterRequestDto request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        UserResponseDto response = userService.register(request, profileImage);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(
            @Valid @RequestBody UserLoginRequestDto request, HttpServletRequest httpRequest) {
        UserResponseDto response = userService.login(request);

        HttpSession session = httpRequest.getSession();
        session.setAttribute("userSession", new UserSessionInfoDto(
                response.getId(),
                response.getLoginId(),
                response.getEmail(),
                response.getPhone(),
                response.getProfileImageUrl(),
                response.getRegion()
        ));

        return ResponseEntity.ok(response);
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