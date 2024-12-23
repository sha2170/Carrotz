package com.carrotzmarket.api.domain.region.controller;

import com.carrotzmarket.api.domain.region.dto.RegionResponseDto;
import com.carrotzmarket.api.domain.region.repository.RegionRepository;
import com.carrotzmarket.db.region.RegionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionRepository regionRepository;

    @GetMapping("/all-regions")
    public List<RegionResponseDto> getAllRegions() {
        List<RegionEntity> rootRegions = regionRepository.findAllRegions()
                .stream()
                .filter(region -> region.getParentRegion() == null) // 최상위 지역만 가져오기
                .collect(Collectors.toList());

        return rootRegions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RegionResponseDto convertToDto(RegionEntity region) {
        return new RegionResponseDto(
                region.getId(),
                region.getName(),
                region.getChildRegions().stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList())
        );
    }
}
