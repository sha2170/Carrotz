package com.carrotzmarket.api.domain.productImage.repository;

import com.carrotzmarket.db.productImage.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
}
