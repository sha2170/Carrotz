package com.carrotzmarket.api.domain.user.service;

import com.carrotzmarket.api.common.error.RegionErrorCode;
import com.carrotzmarket.api.common.error.UserErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.region.repository.RegionRepository;
import com.carrotzmarket.api.domain.user.converter.UserConverter;
import com.carrotzmarket.api.domain.user.dto.UserLoginRequestDto;
import com.carrotzmarket.api.domain.user.dto.UserRegisterRequestDto;
import com.carrotzmarket.api.domain.user.dto.UserResponseDto;
import com.carrotzmarket.api.domain.user.dto.UserUpdateRequestDto;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RegionRepository regionRepository;

    @Value("${default.profile.image:/uploads/profile-images/default-profile.jpg}")
    private String defaultProfileImageUrl;

    @Value("${file.upload.dir:/uploads/profile-images}")
    private String profileImageDir;

    public UserResponseDto register(UserRegisterRequestDto request, MultipartFile profileImage) {
        userRepository.findByLoginId(request.getLoginId())
                .ifPresent(user -> {
                    throw new ApiException(UserErrorCode.USER_ALREADY_EXIST, "이미 존재하는 사용자입니다.");
                });

        if (request.getRegionId() == null) {
            throw new ApiException(RegionErrorCode.INVALID_REGION, "지역 ID는 필수 입력 값입니다.");
        }

        RegionEntity region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new ApiException(RegionErrorCode.INVALID_REGION, "유효하지 않은 지역입니다."));

        String profileImageUrl = saveProfileImage(request.getLoginId(), profileImage);

        UserEntity userEntity = userConverter.toEntity(request, region, profileImageUrl);
        userEntity.setRegion(region.getName());

        userRepository.save(userEntity);

        return userConverter.toResponse(userEntity);
    }

    private String saveProfileImage(String loginId, MultipartFile profileImage) {
        if (profileImage == null || profileImage.isEmpty()) {
            return defaultProfileImageUrl;
        }
        try {
            Path directory = Paths.get(profileImageDir);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            String filename = loginId + "_" + profileImage.getOriginalFilename();
            Path filePath = directory.resolve(filename);
            Files.copy(profileImage.getInputStream(), filePath);
            return "/uploads/profile-images/" + filename;
        } catch (IOException e) {
            throw new ApiException(UserErrorCode.FILE_NOT_UPLOADED, "이미지 저장 중 오류가 발생했습니다.");
        }
    }


    public UserResponseDto login(UserLoginRequestDto request) {
        UserEntity userEntity = userRepository.findByLoginId(request.getLoginId())
                .filter(user -> user.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "잘못된 로그인 정보"));

        return userConverter.toResponse(userEntity);
    }

    public UserResponseDto getUserInfo(String loginId) {
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return userConverter.toResponse(userEntity);
    }

    public UserResponseDto findUserByLoginId(String loginId) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "해당하는 ID는 없습니다."));

        return UserResponseDto.builder()
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .region(user.getRegion())
                .build();
    }

    public void deleteUser(String loginId) {
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "삭제 대상이 없습니다."));

        userRepository.deleteUserRegionsByUserId(userEntity.getId());
        userRepository.deleteByLoginId(loginId);
    }

    public UserResponseDto updateUser(String loginId, UserUpdateRequestDto request, MultipartFile profileImage) {
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Optional.ofNullable(request.getEmail()).ifPresent(userEntity::setEmail);
        Optional.ofNullable(request.getPhone()).ifPresent(userEntity::setPhone);
        Optional.ofNullable(request.getPassword()).ifPresent(userEntity::setPassword);

        if (profileImage != null && !profileImage.isEmpty()) {
            String newImageUrl = saveProfileImage(loginId, profileImage);
            userEntity.setProfileImageUrl(newImageUrl);
        }

        userRepository.save(userEntity);
        return userConverter.toResponse(userEntity);
    }

}
