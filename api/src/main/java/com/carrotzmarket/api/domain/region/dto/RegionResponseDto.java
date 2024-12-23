package com.carrotzmarket.api.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RegionResponseDto {
    private Long id;
    private String name;
    private List<RegionResponseDto> childRegions;
}
