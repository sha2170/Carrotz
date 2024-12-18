package com.carrotzmarket.api.domain.product.service;

import com.carrotzmarket.api.domain.product.dto.ProductDto;
import com.carrotzmarket.api.domain.product.repository.ProductRepository;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 제품 등록
    public Long createProduct(ProductDto productDto) {
        ProductEntity productEntity = dtoToEntity(productDto);
        ProductEntity savedEntity = productRepository.save(productEntity);
        return savedEntity.getId();
    }

    // 제품 조회
    public ProductDto getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        return entityToDto(productEntity);
    }

    // Dto -> Entity 변환 메서드
    private ProductEntity dtoToEntity(ProductDto productDto) {
        return ProductEntity.builder()
                .title(productDto.getTitle())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .userId(1L) // 기본값: 임시 사용자 ID
                .regionId(1L) // 기본값: 임시 지역 ID
                .status(ProductStatus.ON_SALE)
                .viewCount(0) // 기본값
                .favoriteCount(0) // 기본값
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // Entity -> Dto 변환 메서드
    private ProductDto entityToDto(ProductEntity productEntity) {
        return new ProductDto(
                productEntity.getTitle(),
                productEntity.getDescription(),
                productEntity.getPrice()
        );
    }

    // 특정 사용자에 의해 등록된 모든 제품 조회
    public List<ProductEntity> getProductByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    // 제품 이름 부분으로 검색
    public List<ProductEntity> searchProductByTitle(String title) {
        return productRepository.findByTitleContaining(title);
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