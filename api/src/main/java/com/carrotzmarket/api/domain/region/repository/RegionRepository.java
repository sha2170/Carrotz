package com.carrotzmarket.api.domain.region.repository;

import com.carrotzmarket.api.common.error.RegionErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import com.carrotzmarket.db.user.UserRegionEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegionRepository {

    private final EntityManager em;

    // ID로 지역 조회
    public Optional<RegionEntity> findById(Long id) {
        return Optional.ofNullable(em.find(RegionEntity.class, id));
    }

    // 이름으로 지역 조회
    public Optional<RegionEntity> findByName(String name) {
        return em.createQuery("SELECT r FROM RegionEntity r WHERE r.name = :name", RegionEntity.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    // 새로운 지역 저장
    public RegionEntity save(RegionEntity region) {
        if (region.getId() == null) {
            em.persist(region);
        } else {
            em.merge(region);
        }
        return region;
    }

    // 사용자와 지역을 연관짓는 메서드
    public void addUserRegion(Long userId, Long regionId) {
        UserEntity user = em.find(UserEntity.class, userId);
        RegionEntity region = em.find(RegionEntity.class, regionId);

        if (user == null || region == null) {
            throw new ApiException(RegionErrorCode.INVALID_REGION, "유효하지 않은 사용자 또는 지역 ID입니다.");
        }

        UserRegionEntity userRegion = UserRegionEntity.builder()
                .user(user)
                .region(region)
                .build();

        em.persist(userRegion);
    }

    // 모든 지역 조회
    public List<RegionEntity> findAll() {
        return em.createQuery("SELECT r FROM RegionEntity r", RegionEntity.class)
                .getResultList();
    }
}

