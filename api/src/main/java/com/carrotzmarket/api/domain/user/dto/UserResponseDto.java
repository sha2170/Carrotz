package com.carrotzmarket.api.domain.user.dto;

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
public class UserResponseDto {

    /*  password 는 민감 정보이기 때문에 반환하지 않는다.
     *  하지만 개인정보 변경 기능으로 인해 같이 반환해주는 것으로 변경
     */
    private Long id;
    private String loginId;
    private String password;
    private String email;
    private String phone;
    private LocalDate birthday;
    private String profileImageUrl;
    private boolean isDeleted;
    private String region;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
