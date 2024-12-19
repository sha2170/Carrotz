package com.carrotzmarket.api.domain.user.service;

import com.carrotzmarket.api.common.error.RegionErrorCode;
import com.carrotzmarket.api.common.error.UserErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 등록
     * @param user 등록할 사용자 엔티티
     */
    public void register(UserEntity user) {
        // 이미 존재하는 로그인 ID인지 확인
        userRepository.findByLoginId(user.getLoginid())
                .ifPresent(existingUser -> {
                    throw new ApiException(UserErrorCode.USER_NOT_FOUND, "이미 존재하는 로그인 ID입니다.");
                });

        // 사용자 저장
        userRepository.save(user);
    }

    /**
     * 사용자 로그인
     * @param loginId 사용자 로그인 ID
     * @param password 사용자 비밀번호
     * @return 인증된 사용자 엔티티
     */
    public UserEntity login(String loginId, String password) {
        // 로그인 ID로 사용자 조회
        return userRepository.findByLoginId(loginId)
                .filter(user -> user.getPassword().equals(password)) // 비밀번호 확인
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "로그인 정보가 잘못되었습니다."));
    }

    /**
     * 사용자 조회
     * @param userId 조회할 사용자 ID
     * @return 사용자 엔티티
     */
    public UserEntity findById(Long userId) {
        // 사용자 ID로 조회
        return userRepository.findById(userId) // ID를 문자열로 변환 후 조회
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, "해당 ID의 사용자를 찾을 수 없습니다."));
    }

    /**
     * 모든 사용자 조회
     * @return 모든 사용자 엔티티 목록
     */
    public Iterable<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 사용자 삭제
     * @param loginId 삭제할 사용자 ID
     */
    public void deleteUser(String loginId) {
        userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, " 삭제 대상이 없습니다."));

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
}
