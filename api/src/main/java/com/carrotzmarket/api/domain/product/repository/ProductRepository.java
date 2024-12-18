package com.carrotzmarket.api.domain.product.repository;

import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // 특정 사용자에 의해 등록된 모든 제품 조회
    List<ProductEntity> findByUserId(Long userId);

    // 제품 이름의 부분으로 검색
    List<ProductEntity> findByTitleContaining(String title);

    /*// 특정 카테고리에 따라 필터링
    List<ProductEntity> findByCategory_DetailCategory_Id(Long categoryId);
    */

    // 제품 상태에 따라 필터링
    List<ProductEntity> findByStatus(ProductStatus status);

    // 특정 지역에서 판매되는 제품 검색
    List<ProductEntity> findByRegionId(Long regionId);

    // 제품 ID로 제품 검색
    Optional<ProductEntity> findById(Long id);

    // 최근 올라온 제품 10개 목록 정렬
    List<ProductEntity> findTop10ByOrderByCreatedAtDesc();

    // 특정 사용자와 상태 기준으로 제품 검색
    List<ProductEntity> findByUserIdAndStatus(Long userId, ProductStatus status);
}
