package com.carrotzmarket.api.domain.product.service;

import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.repository.ProductRepository;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import com.carrotzmarket.api.domain.region.service.RegionService;
import com.carrotzmarket.db.category.CategoryEntity;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final RegionService regionService;

    public ProductEntity createProduct(ProductCreateRequestDto request) {
        // DTO -> Entity 변환
        ProductEntity product = ProductEntity.builder()
                .id(null)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .userId(request.getUserId())
                .regionId(request.getRegionId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(request.getStatus())
                .build();

        // 카테고리 설정
        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            List<CategoryEntity> categoryEntities = new ArrayList<>();
            for (String categoryName : request.getCategories()) {
                CategoryEntity category = categoryRepository.findByName(categoryName)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + categoryName));
                categoryEntities.add(category);
            }
            product.setCategories(categoryEntities); // 상품에 카테고리 리스트 추가
        }

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

    // 가격 범위로 상품 필터링
    public List<ProductEntity> getProductsByPriceRange(Integer minPrice, Integer maxPrice) {
        if (minPrice == null) minPrice = 0; // 기본 최소값
        if (maxPrice == null) maxPrice = Integer.MAX_VALUE; // 기본 최대값
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // 정렬 범위로 상품 필터링
    public List<ProductEntity> getProductsSortedBy(String sortBy, Integer minPrice, Integer maxPrice) {
        if (minPrice == null) minPrice = 0;
        if (maxPrice == null) maxPrice = Integer.MAX_VALUE;

        if ("newest".equalsIgnoreCase(sortBy)) {
            return productRepository.findByPriceBetweenOrderByCreatedAtDesc(minPrice, maxPrice);
        } else if ("popular".equalsIgnoreCase(sortBy)) {
            return productRepository.findByPriceBetweenOrderByFavoriteCountDesc(minPrice, maxPrice);
        } else {
            throw new IllegalArgumentException("Invalid sortBy value. Use 'newest' or 'popular'.");
        }
    }

    // 카테고리 기반 상품 필터링
    public List<ProductEntity> getProductsByCategory(Long categoryId, Integer minPrice, Integer maxPrice, String sortBy) {
        if (minPrice == null) minPrice = 0;
        if (maxPrice == null) maxPrice = Integer.MAX_VALUE;

        // 하위 카테고리 포함한 ID 목록 조회
        List<Long> categoryIds = getCategoryHierarchy(categoryId);

        // 정렬 조건에 따라 상품 조회
        if ("newest".equalsIgnoreCase(sortBy)) {
            return productRepository.findByCategories_IdInAndPriceBetweenOrderByCreatedAtDesc(categoryIds, minPrice, maxPrice);
        } else if ("popular".equalsIgnoreCase(sortBy)) {
            return productRepository.findByCategories_IdInAndPriceBetweenOrderByFavoriteCountDesc(categoryIds, minPrice, maxPrice);
        } else {
            return productRepository.findByCategories_IdInAndPriceBetween(categoryIds, minPrice, maxPrice);
        }
    }

    // 하위 카테고리 탐색
    private List<Long> getCategoryHierarchy(Long categoryId) {
        // Optional에서 CategoryEntity 추출
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
        List<Long> categoryIds = new ArrayList<>();
        collectCategoryIds(category, categoryIds);
        return categoryIds;
    }

    private void collectCategoryIds(CategoryEntity category, List<Long> categoryIds) {
        categoryIds.add(category.getId());
        for (CategoryEntity child : category.getChildren()) {
            collectCategoryIds(child, categoryIds);
        }
    }

    // 지역 기반 상품 필터링 로직 추가
    public List<ProductEntity> getProductsByRegion(Long regionId, Integer minPrice, Integer maxPrice, String sortBy) {
        if (minPrice == null) minPrice = 0; // 기본 최소값
        if (maxPrice == null) maxPrice = Integer.MAX_VALUE; // 기본 최대값

        // 하위 지역 포함한 ID 목록 조회
        List<Long> regionIds = regionService.getRegionHierarchy(regionId);

        // 정렬 조건에 따라 상품 조회
        if ("newest".equalsIgnoreCase(sortBy)) {
            return productRepository.findByRegionIdInAndPriceBetweenOrderByCreatedAtDesc(regionIds, minPrice, maxPrice);
        } else if ("popular".equalsIgnoreCase(sortBy)) {
            return productRepository.findByRegionIdInAndPriceBetweenOrderByFavoriteCountDesc(regionIds, minPrice, maxPrice);
        } else {
            return productRepository.findByRegionIdInAndPriceBetween(regionIds, minPrice, maxPrice);
        }
    }

}
