package com.carrotzmarket.api.domain.user.converter;

import com.carrotzmarket.api.common.annotation.Converter;
import com.carrotzmarket.api.common.error.ErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.controller.model.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.db.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
@Component
public class UserConverter {


    // DTO -> Entity 변환
    public UserEntity toEntity(UserRegisterRequest request) {
        return UserEntity.builder()
                .loginid(request.getLoginId())
                .password(request.getPassword())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthday(request.getBirthday() != null ? LocalDate.parse(request.getBirthday()) : null)
                .profile_iamge_url(request.getProfileImageUrl())
                .build();
    }

    // Entity -> DTO 변환
    public UserResponse toResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .loginId(userEntity.getLoginid())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .birthday(userEntity.getBirthday())
                .profileImageUrl(userEntity.getProfile_iamge_url())
                .createdAt(userEntity.getCreatedAt())
                .lastLoginAt(userEntity.getLastLoginAt())
                .isDeleted(userEntity.isDeleted())
                .build();
    }
}
