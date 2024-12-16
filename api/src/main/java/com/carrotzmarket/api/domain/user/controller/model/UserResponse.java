package com.carrotzmarket.api.domain.user.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    // 유저에게 반환될 데이터
    // password는 민감정보이기 때문에 반환하지 않는다.
    private String loginId;

    private String email;

    private String phone;

    private LocalDate birthday;

    private String profileImageUrl;

    private boolean isDeleted;

    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

}
