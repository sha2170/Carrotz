package com.carrotzmarket.api.domain.viewedProduct.repository;

import com.carrotzmarket.db.viewedProducts.ViewedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewedProductRepository extends JpaRepository<ViewedProductEntity, Long> {
    Optional<ViewedProductEntity> findByUserIdAndProductId(Long userId, Long productId);
    List<ViewedProductEntity> findByUserId(Long userId);
}
