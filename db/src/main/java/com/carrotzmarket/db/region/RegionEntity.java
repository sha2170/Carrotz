package com.carrotzmarket.db.region;

import com.carrotzmarket.db.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name; // 지역 이름

    // 자기 자신을 참조하는 부모-자식 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // 상위 지역을 참조
    private RegionEntity parentRegion;

    @OneToMany(mappedBy = "parentRegion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegionEntity> childRegions = new ArrayList<>();

}
