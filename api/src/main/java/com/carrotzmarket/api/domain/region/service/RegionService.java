package com.carrotzmarket.api.domain.region.service;

import com.carrotzmarket.api.domain.region.repository.RegionRepository;
import com.carrotzmarket.db.region.RegionEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public void saveRegion(RegionEntity regionEntity) {
        regionRepository.save(regionEntity);
    }

    public RegionEntity getRegionById(Long id) {
        return regionRepository.findById(id);
    }

    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    public List<RegionEntity> getAllRegions() {
        return regionRepository.findAllRegions();
    }
}
