package com.carrotzmarket.api.domain.product.repository;

import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByUserId(Long userId);

    List<ProductEntity> findByTitleContaining(String title);

    List<ProductEntity> findByStatus(ProductStatus status);

    List<ProductEntity> findByRegionId(Long regionId);

    Optional<ProductEntity> findById(Long id);

    List<ProductEntity> findTop10ByOrderByCreatedAtDesc();

    List<ProductEntity> findByUserIdAndStatus(Long userId, ProductStatus status);

    List<ProductEntity> findByCategoryId(Long categoryId);

    List<ProductEntity> findByCategoryNameContaining(String categoryName);

    List<ProductEntity> findAllByOrderByCreatedAtDescUpdatedAtDesc();

    List<ProductEntity> findAllByOrderByCreatedAtDesc();

    List<ProductEntity> findAllByOrderByUpdatedAtDesc();

    List<ProductEntity> findByPriceBetween(int minPrice, int maxPrice);
}
