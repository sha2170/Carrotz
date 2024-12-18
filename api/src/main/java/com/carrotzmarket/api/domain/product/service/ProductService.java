package com.carrotzmarket.api.domain.product.service;

import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.repository.ProductRepository;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductEntity createProduct(ProductCreateRequestDto request) {
        // DTO -> Entity 변환
        ProductEntity product = ProductEntity.builder()
                .id(null)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .userId(request.getUserId())
                .regionId(request.getRegionId())
                .categories(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(request.getStatus())
                .build();
        // 상품 저장
        return productRepository.save(product);
    }

    // 제품 조회
    public ProductEntity getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }

    // Dto -> Entity 변환 메서드
    private ProductEntity dtoToEntity(ProductCreateRequestDto productCreateRequestDto) {
        return ProductEntity.builder()
                .title(productCreateRequestDto.getTitle())
                .description(productCreateRequestDto.getDescription())
                .price(productCreateRequestDto.getPrice())
                .regionId(1L) // 기본값: 임시 지역 ID
                .status(ProductStatus.ON_SALE)
                .viewCount(0) // 기본값
                .favoriteCount(0) // 기본값
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // Entity -> Dto 변환 메서드
    private ProductCreateRequestDto entityToDto(ProductEntity productEntity) {
        return ProductCreateRequestDto.builder()
                .title(productEntity.getTitle())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .userId(productEntity.getUserId())
                .regionId(productEntity.getRegionId())
                .status(productEntity.getStatus())
                .build();
    }

    public List<ProductEntity> getProductByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    public List<ProductEntity> searchProductByName(String name) {
        return productRepository.findByTitleContaining(name);
    }

    public List<ProductEntity> searchProductByTitle(String title) {
        return productRepository.findByTitleContaining(title);
    }

    public List<ProductEntity> getProductByStatus(ProductStatus status) {
        return productRepository.findByStatus(status);
    }


    public List<ProductEntity> getProductByRegion(Long regionId) {
        return productRepository.findByRegionId(regionId);
    }


    public List<ProductEntity> getTop10Products() {
        return productRepository.findTop10ByOrderByCreatedAtDesc();
    }


    public List<ProductEntity> getProductByUserIdAndStatus(Long userId, ProductStatus status) {
        return productRepository.findByUserIdAndStatus(userId, status);
    }

    public List<ProductEntity> getProductByCategory(String categoryName) {
        return productRepository.findByCategories_Name(categoryName);
    }
}
