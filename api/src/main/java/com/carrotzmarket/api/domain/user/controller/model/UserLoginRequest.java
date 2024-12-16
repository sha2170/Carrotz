package com.carrotzmarket.api.domain.user.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    // 로그인 시 입력받을 데이터
    @NotNull
    private String loginId;

    @NotNull
    private String password;
}
