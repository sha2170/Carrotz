package com.carrotzmarket.api.domain.user.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    // 회원 가입 시 받아올 데이터 목록
    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;

    private String phone;

    private String birthday;

    private String profileImageUrl;

}
