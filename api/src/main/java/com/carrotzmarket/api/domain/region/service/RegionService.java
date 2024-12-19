package com.carrotzmarket.api.domain.region.service;

import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import com.carrotzmarket.db.user.UserRegionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RegionService {

    @PersistenceContext
    private EntityManager em;

    // 사용자에 지역 추가
    public void addUserRegion(Long userId, Long regionId) {
        UserEntity user = em.find(UserEntity.class, userId);
        RegionEntity region = em.find(RegionEntity.class, regionId);

        if (user == null || region == null) {
            throw new IllegalArgumentException("User or Region not found.");
        }

        UserRegionEntity userRegion = new UserRegionEntity();
        userRegion.setUser(user);
        userRegion.setRegion(region);

        em.persist(userRegion);
        user.getUserRegions().add(userRegion);
    }

    // 지역 저장
    public void saveRegion(String name, Long parentId){
        RegionEntity region = new RegionEntity();
        region.setName(name);

        // 부모의 id가 Null이 아니면 실행
        if(parentId != null){
            RegionEntity parentregion = em.find(RegionEntity.class, parentId);
            // 부모지역이 Null이 아니라면 실행
            if(parentregion != null){
                region.setParentRegion(parentregion);
                parentregion.getChildRegions().add(region);
            }
        }
        em.persist(region);
    }

    // ID로 지역 조회
    public RegionEntity findById(Long id){
        return em.find(RegionEntity.class, id);
    }

    // 모든 지역 조회
    public List<RegionEntity> findAllRegions(){
        return em.createQuery("SELECT r FROM RegionEntity r", RegionEntity.class)
                .getResultList();
    }

    // 특정 지역 및 하위 지역에 포함된 지역 ID 목록 반환
    public List<Long> getRegionHierarchy(Long regionId) {
        RegionEntity region = findById(regionId);
        if (region == null) {
            throw new IllegalArgumentException("Region with ID " + regionId + " not found.");
        }

        List<Long> regionIds = new ArrayList<>();
        collectRegionIds(region, regionIds);
        return regionIds;
    }

    private void collectRegionIds(RegionEntity region, List<Long> regionIds) {
        regionIds.add(region.getId());
        for (RegionEntity child : region.getChildRegions()) {
            collectRegionIds(child, regionIds);
        }
    }
}
