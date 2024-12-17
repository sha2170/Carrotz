package com.carrotzmarket.api.domain.user.region;

import com.carrotzmarket.api.domain.region.service.RegionService;
import com.carrotzmarket.db.region.RegionEntity;
import com.carrotzmarket.db.user.UserEntity;
import com.carrotzmarket.db.user.UserRegionEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegionTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private RegionService regionService;

    private RegionEntity parentRegion;
    private RegionEntity childRegion;

    @BeforeEach
    void setUp(){
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
        when(em.find(RegionEntity.class, 1L)).thenReturn(parentRegion);


        // When
        regionService.saveRegion("child region", 1L);


        // Then
        verify(em, times(1)).persist(any(RegionEntity.class));
    }

    @Test
    void 사용자_지역_등록_성공() {

        // Given
        UserEntity user = new UserEntity();
        user.setId(1L);
        RegionEntity region = new RegionEntity();
        region.setId(1L);

        when(em.find(UserEntity.class, 1L)).thenReturn(user);
        when(em.find(RegionEntity.class, 1L)).thenReturn(region);

        // When
        regionService.addUserRegion(1L, 1L);


        // Then
        verify(em, times(1)).persist(any(UserRegionEntity.class));

    }
}
