package com.carrotzmarket.api.domain.user.service;

import com.carrotzmarket.api.common.api.Api;
import com.carrotzmarket.api.common.error.RegionErrorCode;
import com.carrotzmarket.api.common.error.UserErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.controller.model.UserLoginRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.api.domain.user.controller.model.UserUpdateRequest;
import com.carrotzmarket.api.domain.user.converter.UserConverter;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import com.carrotzmarket.db.user.UserRegionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;


    public Api<UserResponse> register(UserRegisterRequest request){

        UserEntity userEntity = userConverter.toEntity(request);
        userEntity.setCreatedAt(LocalDateTime.now());

        userRepository.findByLoginId(userEntity.getLoginid())
                .ifPresent(existingUser -> {
                    throw new ApiException(UserErrorCode.USER_ALREADY_EXIST, "이미 존재하는 로그인 아이디 입니다.");
                });

        userRepository.save(userEntity);

        UserResponse response = userConverter.toResponse(userEntity);
        return Api.OK(response);
    }

    public Api<UserResponse> login(UserLoginRequest request) {
        UserEntity userEntity = userRepository.findByLoginId(request.getLoginId())
                .filter(user -> user.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "절못된 로그인 정보"));

        UserResponse response = userConverter.toResponse(userEntity);
        return Api.OK(response);
    }

    public UserResponse getUserInfo(String loginId) {
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(()-> new ApiException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return userConverter.toResponse(userEntity);
    }

    public UserResponse updateUser(String loginId, UserUpdateRequest request) {
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        Optional.ofNullable(request.getEmail()).ifPresent(userEntity::setEmail);
        Optional.ofNullable(request.getPhone()).ifPresent(userEntity::setPhone);
        Optional.ofNullable(request.getPassword()).ifPresent(userEntity::setPassword);
        Optional.ofNullable(request.getProfileImageUrl()).ifPresent(userEntity::setProfile_iamge_url);

        if(request.getRegionId() != null){
            RegionEntity region = userRepository.findRegionById(request.getRegionId())
                    .orElseThrow(() -> new ApiException(RegionErrorCode.REGION_NOT_FOUND, "해당 지역을 찾지 못했습니다."));
            userEntity.getUserRegions().clear();
            userEntity.getUserRegions().add(UserRegionEntity.builder().user(userEntity).region(region).build());
        }

        userRepository.save(userEntity);
        return userConverter.toResponse(userEntity);
    }


    public void deleteUser(String loginId) {
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, " 삭제 대상이 없습니다."));

        // 지역도 같이 삭제해줘야 유저가 삭제됨
        userRepository.deleteUserRegionsByUserId(userEntity.getId());
        userRepository.deleteByLoginId(loginId);
    }

    // 사용자 정보 업데이트용 save 메서드
    public void save(UserEntity user){
        userRepository.save(user);
    }

    public RegionEntity findRegionById(Long regionId){
        return userRepository.findRegionById(regionId)
                .orElseThrow(() -> new ApiException(RegionErrorCode.REGION_NOT_FOUND,"해당 지역을 찾을 수 없음"));
    }

    public UserEntity findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "해당 로그인 ID의 사용자를 찾을 수 없습니다."));
    }

    public UserEntity findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "해당 ID의 사용자를 찾을 수 없습니다."));
    }
}