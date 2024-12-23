package com.carrotzmarket.api.domain.user.converter;

import com.carrotzmarket.api.domain.region.repository.RegionRepository;
import com.carrotzmarket.api.domain.user.dto.UserRegisterRequestDto;
import com.carrotzmarket.api.domain.user.dto.UserResponseDto;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final RegionRepository regionRepository;

    @Value("/path/to/uploads")
    private String uploadDir;

    @Value("${default.profile.image:/uploads/profile-images/default-profile.jpg}")
    private String defaultProfileImageUrl;

    // DTO -> Entity 변환
    public UserEntity toEntity(UserRegisterRequestDto request, RegionEntity region, String profileImageUrl) {
        return UserEntity.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthday(request.getBirthday())
                .profileImageUrl(profileImageUrl)
                .region(region.getName())
                .build();
    }

    // Entity -> DTO 변환
    public UserResponseDto toResponse(UserEntity userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .loginId(userEntity.getLoginId())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .profileImageUrl(userEntity.getProfileImageUrl())
                .region(userEntity.getRegion())
                .createdAt(userEntity.getCreatedAt())
                .lastLoginAt(userEntity.getLastLoginAt())
                .build();
    }
}
