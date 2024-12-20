package com.carrotzmarket.api.domain.favoriteProduct.repository;

import com.carrotzmarket.db.favoriteProduct.FavoriteProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProductEntity, Long> {
    Optional<FavoriteProductEntity> findByUserIdAndProductId(Long userId, Long productId);
    List<FavoriteProductEntity> findByUserId(Long userId);
}