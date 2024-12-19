package com.carrotzmarket.api.domain.user.converter;

import com.carrotzmarket.api.common.error.RegionErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.region.service.RegionService;
import com.carrotzmarket.api.domain.user.controller.model.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.controller.model.UserResponse;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import com.carrotzmarket.db.user.UserRegionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final RegionService regionService;

    // DTO -> Entity 변환
    public UserEntity toEntity(UserRegisterRequest request) {
        if (request.getRegionId() == null) {
            throw new ApiException(RegionErrorCode.INVALID_REGION, "지역이 제공되지 않음");
        }
        RegionEntity region = regionService.findById(request.getRegionId());
        if (region == null) {
            throw new ApiException(RegionErrorCode.INVALID_REGION, "유효하지 않은 지역.");
        }

        UserEntity userEntity = UserEntity.builder()
                .loginid(request.getLoginId())
                .password(request.getPassword())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthday(request.getBirthday() != null ? (request.getBirthday()) : null)
                .profile_iamge_url(request.getProfileImageUrl())
                .build();


        UserRegionEntity userRegion = UserRegionEntity.builder()
                .user(userEntity)
                .region(region)
                .build();

        // 지역이 null인지 확인하고 초기화
        if (userEntity.getUserRegions() == null) {
            userEntity.setUserRegions(new ArrayList<>());
        }
        userEntity.getUserRegions().add(userRegion);

        return userEntity;
    }

    // Entity -> DTO 변환
    public UserResponse toResponse(UserEntity userEntity) {
        // 지역의 첫 번째 값만 가져오기
        String regionName = userEntity.getUserRegions() != null && !userEntity.getUserRegions().isEmpty()
                ? userEntity.getUserRegions().get(0).getRegion().getName()
                : null;

        return UserResponse.builder()
                .loginId(userEntity.getLoginid())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .birthday(userEntity.getBirthday())
                .profileImageUrl(userEntity.getProfile_iamge_url())
                .createdAt(userEntity.getCreatedAt())
                .lastLoginAt(userEntity.getLastLoginAt())
                .isDeleted(userEntity.isDeleted())
                .regionName(userEntity.getUserRegions() != null && !userEntity.getUserRegions().isEmpty() ? userEntity.getUserRegions().get(0).getRegion().getName() : null).
                build();
    }
}
