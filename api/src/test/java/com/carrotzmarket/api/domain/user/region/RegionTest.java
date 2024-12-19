package com.carrotzmarket.api.domain.user.region;

import com.carrotzmarket.api.domain.region.repository.RegionRepository;
import com.carrotzmarket.api.domain.region.service.RegionService;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RegionTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    private RegionEntity parentRegion;
    private RegionEntity childRegion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        parentRegion = new RegionEntity();
        parentRegion.setId(1L);
        parentRegion.setName("parent region");

        childRegion = new RegionEntity();
        childRegion.setId(2L);
        childRegion.setName("child region");
        childRegion.setParentRegion(parentRegion);
    }

    @Test
    void 지역_저장_성공() {
        // Given
        when(regionRepository.findById(1L)).thenReturn(Optional.of(parentRegion));
        when(regionRepository.save(any(RegionEntity.class))).thenReturn(childRegion);

        // When
        RegionEntity savedRegion = regionService.addRegion("child region", 1L);

        // Then
        assertNotNull(savedRegion);
        assertEquals("child region", savedRegion.getName());
        verify(regionRepository, times(1)).save(any(RegionEntity.class));
    }

    @Test
    void 사용자_지역_등록_성공() {
        // Given
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(regionRepository.findById(1L)).thenReturn(Optional.of(parentRegion));

        // When
        regionRepository.addUserRegion(1L, 1L);

        // Then
        verify(regionRepository, times(1)).addUserRegion(1L, 1L);
    }
}

