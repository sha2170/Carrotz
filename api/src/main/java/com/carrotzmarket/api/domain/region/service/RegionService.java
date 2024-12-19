package com.carrotzmarket.api.domain.region.service;

import com.carrotzmarket.api.common.error.RegionErrorCode;
import com.carrotzmarket.api.common.exception.ApiException;
import com.carrotzmarket.api.domain.region.repository.RegionRepository;
import com.carrotzmarket.db.region.RegionEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionEntity findById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new ApiException(RegionErrorCode.INVALID_REGION, "유효하지 않은 지역 ID입니다."));
    }

    public RegionEntity findByName(String name) {
        return regionRepository.findByName(name)
                .orElseThrow(() -> new ApiException(RegionErrorCode.INVALID_REGION, "지역 이름이 유효하지 않습니다."));
    }

    @Transactional
    public RegionEntity addRegion(String name, Long parentId) {
        RegionEntity parentRegion = null;
        if (parentId != null) {
            parentRegion = findById(parentId);
        }

        RegionEntity region = RegionEntity.builder()
                .name(name)
                .parentRegion(parentRegion)
                .build();

        return regionRepository.save(region);
    }

    public List<RegionEntity> findAllRegions() {
        return regionRepository.findAll();
    }

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
