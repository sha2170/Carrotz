package com.carrotzmarket.api.domain.user.service;

import com.carrotzmarket.api.common.error.RegionErrorCode;
import com.carrotzmarket.api.common.error.UserErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import com.carrotzmarket.db.user.UserRegionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

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
     * @param userId 삭제할 사용자 ID
     */
    public void deleteUser(Long userId) {
        // 사용자 조회 후 삭제
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND, "삭제하려는 사용자가 존재하지 않습니다.");
        }
    }

    public void addUserRegion(Long userId, Long regionId){
        UserEntity user = em.find(UserEntity.class, userId);
        RegionEntity region = em.find(RegionEntity.class, regionId);

        /*두 아이디 중 하나라도 없으면 예외 발생
         *지역을 입력받는 아이디와 입력할 지역이 있어야하기 때문
         **/
        if(user == null || region == null){
            throw new ApiException(RegionErrorCode.REGION_NOT_FOUND, "해당 지역을 찾을 수 없습니다.");
        }

        UserRegionEntity userRegion = UserRegionEntity.builder()
                .user(user)
                .region(region)
                .build();

        user.getUserRegions().add(userRegion);
        em.persist(userRegion);
    }
}
