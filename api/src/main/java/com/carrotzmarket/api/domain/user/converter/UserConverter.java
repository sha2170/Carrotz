package com.carrotzmarket.api.domain.user.converter;

import com.carrotzmarket.api.common.annotation.Converter;
import com.carrotzmarket.api.common.error.ErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.controller.model.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.db.user.UserEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Converter
public class UserConverter {

    public UserEntity toEntity(UserRegisterRequest request) {

        return Optional.ofNullable(request)
                .map(it -> {

                    return UserEntity.builder()
                            .loginid(request.getLoginId())
                            .email(request.getEmail())
                            .password(request.getPassword())
                            .build();
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "UserRegisterRequest is null"));
    }

    public UserResponse toResponse(UserEntity userEntity) {

        return Optional.ofNullable(userEntity)
                .map(it -> {

                    return UserResponse.builder()

                })
    }
}
