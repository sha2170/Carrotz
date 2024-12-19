package com.carrotzmarket.api.domain.region.controller;

import com.carrotzmarket.api.domain.region.service.RegionService;
import com.carrotzmarket.db.region.RegionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RegionController
 * - 지역 관련 API 엔드포인트 제공
 */
@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionEntity>> getAllRegions() {
        return ResponseEntity.ok(regionService.findAllRegions());
    }

    @GetMapping("/{id}/hierarchy")
    public ResponseEntity<List<Long>> getRegionHierarchy(@PathVariable("regionId") Long regionId) {
        return ResponseEntity.ok(regionService.getRegionHierarchy(regionId));
    }

//    @PostMapping("/user")
//    public ResponseEntity<String> addUserRegion(@RequestParam Long userId, @RequestParam Long regionId) {
//        regionService.addUserRegion(userId, regionId);
//        return ResponseEntity.ok("Region added to user successfully.");
//    }

}
