package com.carrotzmarket.api.domain.product.service;

import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.repository.ProductRepository;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import com.carrotzmarket.db.category.CategoryEntity;
import com.carrotzmarket.api.domain.product.dto.ProductRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;  // 카테고리 레포지토리 추가

    /**
     * 상품 등록
     * @param productRequestDto 상품 등록 요청 데이터 (상품명, 가격, 설명, 카테고리명 등)
     * @return 등록된 상품 정보
     */
    public ProductEntity createProduct(ProductRequestDto productRequestDto) {
        // 여러 카테고리명을 기준으로 카테고리 조회
        List<CategoryEntity> categories = productRequestDto.getCategoryNames().stream()
                .map(categoryName -> categoryRepository.findByName(categoryName))
                .collect(Collectors.toList());

        // ProductEntity 생성
        ProductEntity product = new ProductEntity();
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setUserId(productRequestDto.getUserId());
        product.setRegionId(productRequestDto.getRegionId());
        product.setCategories(categories);  // 여러 카테고리 설정

        // 상품 저장
        return productRepository.save(product);
    }

    /**
     * 상품 ID로 상품 조회
     * @param id 조회할 상품 ID
     * @return 상품 정보
     */
    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    // 제품 등록
    public Long createProduct(ProductCreateRequestDto productCreateRequestDto) {
        ProductEntity productEntity = dtoToEntity(productCreateRequestDto);
        ProductEntity savedEntity = productRepository.save(productEntity);
        return savedEntity.getId();
    }

    // 제품 조회
    public ProductCreateRequestDto getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        return entityToDto(productEntity);
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
        return new ProductCreateRequestDto(
                productEntity.getTitle(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getUserId(),
                productEntity.getRegionId(),
                productEntity.getStatus()
        );
    }

    /**
     * 특정 사용자에 의해 등록된 모든 상품 조회
     * @param userId 사용자 ID
     * @return 상품 목록
     */
    public List<ProductEntity> getProductByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    /**
     * 상품명으로 상품 검색
     * @param name 상품명
     * @return 상품 목록
     */
    public List<ProductEntity> searchProductByName(String name) {
        return productRepository.findByNameContaining(name);
    // 제품 이름 부분으로 검색
    public List<ProductEntity> searchProductByTitle(String title) {
        return productRepository.findByTitleContaining(title);
    }

    /**
     * 제품 상태로 검색
     * @param status 제품 상태
     * @return 상품 목록
     */
    public List<ProductEntity> getProductByStatus(ProductStatus status) {
        return productRepository.findByStatus(status);
    }

    /**
     * 특정 지역으로 상품 검색
     * @param regionId 지역 ID
     * @return 상품 목록
     */
    public List<ProductEntity> getProductByRegion(Long regionId) {
        return productRepository.findByRegionId(regionId);
    }

    /**
     * 최근 올라온 상위 10개 제품 조회
     * @return 상품 목록
     */
    public List<ProductEntity> getTop10Products() {
        return productRepository.findTop10ByOrderByCreatedAtDesc();
    }

    /**
     * 특정 사용자와 상태 기준으로 상품 검색
     * @param userId 사용자 ID
     * @param status 제품 상태
     * @return 상품 목록
     */
    public List<ProductEntity> getProductByUserIdAndStatus(Long userId, ProductStatus status) {
        return productRepository.findByUserIdAndStatus(userId, status);
    }

    /**
     * 카테고리명으로 상품 검색
     * @param categoryName 카테고리명
     * @return 상품 목록
     */
    public List<ProductEntity> getProductByCategory(String categoryName) {
        return productRepository.findByCategory_Name(categoryName);
    }
}
