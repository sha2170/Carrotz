package com.carrotzmarket.api.domain.user.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionInfo {

    // 세션에 저장할 사용자 정보
    private Long id;
    private String loginId;
    private String email;
}
