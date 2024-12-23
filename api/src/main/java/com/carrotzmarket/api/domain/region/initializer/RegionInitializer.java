package com.carrotzmarket.api.domain.region.initializer;

import com.carrotzmarket.api.domain.region.repository.RegionRepository;
import com.carrotzmarket.db.region.RegionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegionInitializer implements CommandLineRunner {

    private final RegionRepository regionRepository; // 직접 구현한 RegionRepository

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (regionRepository.findAllRegions().isEmpty()) {
            RegionEntity seoul = RegionEntity.builder().name("서울시").build();

            RegionEntity gangbuk = RegionEntity.builder().name("강북구").parentRegion(seoul).build();
            List<RegionEntity> gangbukChildren = List.of(
                    RegionEntity.builder().name("미아동").parentRegion(gangbuk).build(),
                    RegionEntity.builder().name("번동").parentRegion(gangbuk).build(),
                    RegionEntity.builder().name("수유동").parentRegion(gangbuk).build(),
                    RegionEntity.builder().name("우이동").parentRegion(gangbuk).build(),
                    RegionEntity.builder().name("인수동").parentRegion(gangbuk).build()
            );

            RegionEntity gangnam = RegionEntity.builder().name("강남구").parentRegion(seoul).build();
            List<RegionEntity> gangnamChildren = List.of(
                    RegionEntity.builder().name("개포동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("논현동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("대치동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("도곡동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("삼성동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("세곡동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("수서동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("신사동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("압구정동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("역삼동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("일원동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("자곡동").parentRegion(gangnam).build(),
                    RegionEntity.builder().name("청담동").parentRegion(gangnam).build()
            );

            regionRepository.save(seoul);
            regionRepository.save(gangbuk);
            regionRepository.save(gangnam);
            gangbukChildren.forEach(regionRepository::save);
            gangnamChildren.forEach(regionRepository::save);
        }
    }
}
