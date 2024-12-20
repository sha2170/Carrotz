package com.carrotzmarket.api.domain.productImage.service;

import com.carrotzmarket.db.productImage.ProductImageEntity;
import com.carrotzmarket.api.domain.productImage.repository.ProductImageRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    // 이미지 저장
    public ProductImageEntity saveProductImage(ProductImageEntity productImage) {
        return productImageRepository.save(productImage);
    }

    public void saveAll(List<ProductImageEntity> productImages) {
        productImageRepository.saveAll(productImages);
    }

    // 이미지 조회
    public List<ProductImageEntity> getProductImageByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }
}