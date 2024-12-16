package com.carrotzmarket.api.domain.user.business;

import com.carrotzmarket.api.common.annotation.Business;
import com.carrotzmarket.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class UserBusiness {

    private final UserService userService;
    private final UserConverter userConverter;
    private final TokenBusiness tokenBusiness;

    public UserResponse register(UserRegisterRequest request) {
        var entity = userConverter.toEntity(request);
        var newEntity = userService.register(entity);
        return userConverter.toResponse(newEntity);
    }

    public TokenResponse login(UserLoginRequest request) {
        var userEntity = userService.login(request.getEmail(), request.getPassword());
        return tokenBusiness.issueToken(userEntity);
    }

    public UserResponse me(Long userId) {
        var userEntity = userService.getUserWithThrow(userId);
        return userConverter.toResponse(userEntity);
    }
}

