package com.carrotzmarket.api.domain.region.repository;

import com.carrotzmarket.db.region.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {

    Optional<RegionEntity> findByName(String name);
}
