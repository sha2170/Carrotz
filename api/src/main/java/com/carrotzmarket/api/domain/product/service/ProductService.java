package com.carrotzmarket.api.domain.product.service;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.dto.ProductResponseDto;
import com.carrotzmarket.api.domain.product.dto.ProductUpdateRequestDto;
import com.carrotzmarket.api.domain.product.repository.ProductRepository;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import com.carrotzmarket.api.domain.region.service.RegionService;
import com.carrotzmarket.db.category.CategoryEntity;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;

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

    public ProductEntity findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }

    public ProductEntity createProduct(ProductCreateRequestDto request) {
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));

        ProductEntity product = ProductEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .userId(request.getUserId())
                .regionId(request.getRegionId())
                .category(category)
                .status(request.getStatus())
                .build();

        return productRepository.save(product);
    }

    public ProductResponseDto getProductById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        CategoryDto categoryDto = product.getCategory() != null ?
                new CategoryDto(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription(), product.getCategory().isEnabled()) :
                null;

        return new ProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getUserId(),
                product.getRegionId(),
                categoryDto,
                product.getStatus()
        );
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto request) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        productRepository.save(product);

        CategoryDto categoryDto = product.getCategory() != null ?
                new CategoryDto(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription(), product.getCategory().isEnabled()) :
                null;

        return new ProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getUserId(),
                product.getRegionId(),
                categoryDto,
                product.getStatus()
        );
    }

    @Transactional
    public ProductResponseDto updateProductStatus(Long id, ProductStatus status) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        product.setStatus(status);
        productRepository.save(product);

        CategoryDto categoryDto = product.getCategory() != null ?
                new CategoryDto(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription(), product.getCategory().isEnabled()) :
                null;

        return new ProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getUserId(),
                product.getRegionId(),
                categoryDto,
                product.getStatus()
        );
    }

    public void deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
        productRepository.delete(product);
    }

    public List<ProductEntity> getProductByUserId(Long userId) {
        return productRepository.findByUserId(userId);
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

    public List<ProductEntity> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<ProductEntity> getProductsByCategoryName(String categoryName) {
        return productRepository.findByCategoryNameContaining(categoryName);
    }
}
