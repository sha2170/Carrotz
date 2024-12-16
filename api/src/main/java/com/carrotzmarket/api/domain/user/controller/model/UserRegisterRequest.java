package com.carrotzmarket.api.domain.user.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    // 회원 가입 시 받아올 데이터 목록
    @NotNull
    private String loginId;

    @NotNull
    private String password;

    @NotNull
    @Email
    private String email;

}
