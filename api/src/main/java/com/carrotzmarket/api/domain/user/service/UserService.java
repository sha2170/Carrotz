package com.carrotzmarket.api.domain.user.service;

import com.carrotzmarket.api.common.error.RegionErrorCode;
import com.carrotzmarket.api.common.error.UserErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.region.repository.RegionRepository;
import com.carrotzmarket.api.domain.user.converter.UserConverter;
import com.carrotzmarket.api.domain.user.dto.UserLoginRequest;
import com.carrotzmarket.api.domain.user.dto.UserRegisterRequest;
import com.carrotzmarket.api.domain.user.dto.UserResponse;
import com.carrotzmarket.api.domain.user.dto.UserUpdateRequest;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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

    @Value("${file.dir:/default/path/to/uploads}")
    private String profileImageDir;

    public UserResponse register(UserRegisterRequest request, MultipartFile profileImage) {
        userRepository.findByLoginId(request.getLoginId())
                .ifPresent(existingUser -> {
                    throw new ApiException(UserErrorCode.USER_ALREADY_EXIST, "이미 존재하는 로그인 아이디 입니다.");
                });

        UserEntity userEntity = userConverter.toEntity(request);

        if (profileImage == null || profileImage.isEmpty()) {
            userEntity.setProfileImageUrl(defaultProfileImageUrl);
        } else {
            userEntity.setProfileImageUrl(saveProfileImage(request.getLoginId(), profileImage));
        }

        RegionEntity region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new ApiException(RegionErrorCode.INVALID_REGION, "유효하지 않은 지역입니다."));
        userEntity.setRegion(region.getName());

        userRepository.save(userEntity);
        return userConverter.toResponse(userEntity);
    }

    private String saveProfileImage(String loginId, MultipartFile profileImage) {
        try {
            String filename = loginId + "_" + StringUtils.cleanPath(profileImage.getOriginalFilename());
            Path filePath = Paths.get(profileImageDir).resolve(filename);
            Files.copy(profileImage.getInputStream(), filePath);
            return "/uploads/profile-images/" + filename;
        } catch (IOException e) {
            throw new ApiException(UserErrorCode.FILE_NOT_UPLOADED, "프로필 이미지 업로드 실패");
        }
    }

    public UserResponse login(UserLoginRequest request) {
        UserEntity userEntity = userRepository.findByLoginId(request.getLoginId())
                .filter(user -> user.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "잘못된 로그인 정보"));

        return userConverter.toResponse(userEntity);
    }

    public UserResponse getUserInfo(String loginId) {
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return userConverter.toResponse(userEntity);
    }

    public UserResponse findUserByLoginId(String loginId) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "해당하는 ID는 없습니다."));

        return UserResponse.builder()
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

    public UserResponse updateUser(String loginId, UserUpdateRequest request, MultipartFile profileImage) {
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Optional.ofNullable(request.getEmail()).ifPresent(userEntity::setEmail);
        Optional.ofNullable(request.getPhone()).ifPresent(userEntity::setPhone);
        Optional.ofNullable(request.getPassword()).ifPresent(userEntity::setPassword);

        if (request.getRegionId() != null) {
            RegionEntity region = userRepository.findRegionById(request.getRegionId())
                    .orElseThrow(() -> new ApiException(RegionErrorCode.REGION_NOT_FOUND, "해당 지역을 찾지 못했습니다."));
            userEntity.setRegion(region.getName());
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                Optional.ofNullable(userEntity.getProfileImageUrl()).ifPresent(existingImage -> {
                    try {
                        Path oldFilePath = Paths.get(profileImageDir).resolve(existingImage);
                        Files.deleteIfExists(oldFilePath);
                    } catch (IOException e) {
                        throw new ApiException(UserErrorCode.FILE_NOT_UPLOADED, "기존 이미지 삭제 실패");
                    }
                });

                String filename = loginId + "_" + StringUtils.cleanPath(profileImage.getOriginalFilename());
                Path newFilePath = Paths.get(profileImageDir).resolve(filename);
                Files.copy(profileImage.getInputStream(), newFilePath);

                userEntity.setProfileImageUrl("/uploads/profile-images/" + filename);
            } catch (IOException e) {
                throw new ApiException(UserErrorCode.FILE_NOT_UPLOADED, "프로필 이미지 업로드 실패");
            }
        }

        userRepository.save(userEntity);
        return userConverter.toResponse(userEntity);
    }
}
