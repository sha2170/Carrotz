package com.carrotzmarket.api.domain.user.business;

import com.carrotzmarket.api.common.annotation.Business;
import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.common.error.UserErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.controller.model.UserLoginRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.api.domain.user.controller.model.UserUpdateRequest;
import com.carrotzmarket.api.domain.user.converter.UserConverter;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.api.domain.user.service.UserService;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import com.carrotzmarket.db.user.UserRegionEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Business
@RequiredArgsConstructor
public class UserBusiness {

    private final UserService userService;
    private final UserConverter userConverter;
    private final UserRepository userRepository;

    // 사용자 등록 로직
    public Api<UserResponse> register(UserRegisterRequest request) {
        UserEntity userEntity = userConverter.toEntity(request); // DTO -> Entity 변환
        userEntity.setCreatedAt(LocalDateTime.now());
        userService.register(userEntity); // 사용자 저장
        UserResponse response = userConverter.toResponse(userEntity); // Entity -> DTO 변환
        return Api.OK(response); // 응답 성공 포맷으로 변환
    }

    // 사용자 로그인 로직
    public Api<UserResponse> login(UserLoginRequest request) {
        UserEntity userEntity = userService.login(request.getLoginId(), request.getPassword());
        if (userEntity == null) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND); // 사용자 미발견 예외
        }
        UserResponse response = userConverter.toResponse(userEntity);
        return Api.OK(response);
    }

    public UserResponse getUserInfo(Long userId) {
        // UserEntity를 userId로 조회
        UserEntity userEntity = userService.findById(userId);

        // 조회한 Entity를 Response DTO로 변환
        return userConverter.toResponse(userEntity);
    }


    // 사용자 정보에 지역 추가
    public void addUserRegion(Long userId, Long regionId) {
        userRepository.addUserRegion(userId, regionId);
    }

    public UserResponse updateUser(String loginId, UserUpdateRequest request) {
        UserEntity userEntity = userService.findByLoginId(loginId);

        // 이메일, 전화번호, 비밀번호, 프로필사진 업데이트
        if(request.getEmail() != null) {
            userEntity.setEmail(request.getEmail());
        }
        if(request.getPhone() != null) {
            userEntity.setPhone(request.getPhone());
        }
        if(request.getPassword() != null) {
            userEntity.setPassword(request.getPassword());
        }
        if(request.getProfileImageUrl() != null){
            userEntity.setProfile_iamge_url(request.getProfileImageUrl());
        }

        // 지역 업데이트
        if(request.getRegionId() != null) {
            RegionEntity region = userService.findRegionById(request.getRegionId());
            userEntity.getUserRegions().clear();
            userEntity.getUserRegions().add(UserRegionEntity.builder().user(userEntity).region(region).build());
        }
        userService.save(userEntity);

        return userConverter.toResponse(userEntity);

    }

    // 사용자 탈퇴
    public void deleteUser(String loginId) {
        userService.deleteUser(loginId);
    }
}