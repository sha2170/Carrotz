package com.carrotzmarket.api.domain.productImage.service;

import com.carrotzmarket.db.productImage.ProductImageEntity;
import com.carrotzmarket.api.domain.productImage.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    // 이미지 저장
    public ProductImageEntity saveProductImage(ProductImageEntity productImageEntity) {
        return productImageRepository.save(productImageEntity);
    }
}
