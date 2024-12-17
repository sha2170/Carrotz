package com.carrotzmarket.api.domain.product.service;

import com.carrotzmarket.api.domain.product.repository.ProductRepository;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 제품 등록
    public ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    // 제품 조회
    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // 특정 사용자에 의해 등록된 모든 제품 조회
    public List<ProductEntity> getProductByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    // 제품 이름 부분으로 검색
    public List<ProductEntity> searchProductByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    /*// 특정 카테고리로 검색
    public List<ProductEntity> getProductByCategory(Long categoryId) {
        return productRepository.findByCategory_DetailCategory_Id(categoryId);
    }

*/
    // 제품 상태로 검색
    public List<ProductEntity> getProductByStatus(ProductStatus status) {
        return productRepository.findByStatus(status);
    }

    // 특정 지역으로 검색
    public List<ProductEntity> getProductByRegion(Long regionId) {
        return productRepository.findByRegionId(regionId);
    }

    // 최근 올라온 제품 10개 조회
    public List<ProductEntity> getTop10Products() {
        return productRepository.findTop10ByOrderByCreatedAtDesc();
    }

    // 특정 사용자와 상태 기준으로 검색
    public List<ProductEntity> getProductByUserIdAndStatus(Long userId, ProductStatus status) {
        return productRepository.findByUserIdAndStatus(userId, status);
    }
}