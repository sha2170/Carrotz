package com.carrotzmarket.api.domain.product.service;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;

import com.carrotzmarket.api.domain.favoriteProduct.repository.FavoriteProductRepository;
import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.dto.ProductResponseDto;
import com.carrotzmarket.api.domain.product.dto.ProductUpdateRequestDto;
import com.carrotzmarket.api.domain.product.repository.ProductRepository;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import com.carrotzmarket.api.domain.productImage.service.FileUploadService;
import com.carrotzmarket.api.domain.productImage.service.ProductImageService;
import com.carrotzmarket.api.domain.region.service.RegionService;
import com.carrotzmarket.db.category.CategoryEntity;
import com.carrotzmarket.db.favoriteProduct.FavoriteProductEntity;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;

import java.util.Comparator;
import java.util.List;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.carrotzmarket.db.productImage.ProductImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RegionService regionService;
    private final ProductImageService productImageService;
    private final FileUploadService fileUploadService;
    private final FavoriteProductRepository favoriteProductRepository;

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

        // 상품 저장
        ProductEntity savedProduct = productRepository.save(product);

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<ProductImageEntity> productImages = new ArrayList<>();
            for (MultipartFile image : request.getImages()) {
                try {
                    String imageUrl = fileUploadService.uploadFile(image);
                    ProductImageEntity productImage = new ProductImageEntity();
                    productImage.setProductId(savedProduct.getId());
                    productImage.setImageUrl(imageUrl);
                    productImages.add(productImage);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image: " + image.getOriginalFilename(), e);
                }
            }
            productImageService.saveAll(productImages);
        }

        return savedProduct;
    }


    // 제품 조회
    public ProductResponseDto getProductById(Long id) {
        ProductEntity product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new RuntimeException("이 상품은 등록되지 않은 상품입니다.");
        }

        List<ProductImageEntity> productImages = productImageService.getProductImageByProductId(id);
        List<String> imageUrls = productImages.stream()
                .map(ProductImageEntity::getImageUrl)
                .collect(Collectors.toList());

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
                product.getStatus(),
                imageUrls
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
        productRepository.save(product); 

        List<String> imageUrls = productImageService.getProductImageByProductId(id).stream()
                .map(ProductImageEntity::getImageUrl)
                .collect(Collectors.toList());

        return new ProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getUserId(),
                product.getRegionId(),
                categoryDto,
                product.getStatus()
                product.getStatus(),
                imageUrls
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
        List<String> imageUrls = productImageService.getProductImageByProductId(id).stream()
                .map(ProductImageEntity::getImageUrl)
                .collect(Collectors.toList());

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

                product.getStatus(),
                imageUrls
        );
    }

    // 관심 상품 등록
    @Transactional
    public String addFavoriteProduct(Long userId, Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        Optional<FavoriteProductEntity> existingFavorite = favoriteProductRepository.findByUserIdAndProductId(userId, productId);
        if (existingFavorite.isPresent()) {
            return "이미 관심 상품으로 등록되어 있습니다.";
        }

        FavoriteProductEntity favorite = FavoriteProductEntity.builder()
                .userId(userId)
                .product(product)
                .build();
        favoriteProductRepository.save(favorite);

        return "관심 상품으로 등록되었습니다.";
    }


    // 관심 상품 해제
    @Transactional
    public String removeFavoriteProduct(Long userId, Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        Optional<FavoriteProductEntity> existingFavorite =
                favoriteProductRepository.findByUserIdAndProductId(userId, productId);

        if (existingFavorite.isEmpty()) {
            return "해당 상품은 관심 상품으로 등록되어 있지 않습니다.";
        }

        favoriteProductRepository.delete(existingFavorite.get());
        return "관심 상품이 정상적으로 해제되었습니다.";
    }


    // 관심 상품 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getFavoriteProductsByUserId(Long userId) {
        List<FavoriteProductEntity> favoriteProducts = favoriteProductRepository.findByUserId(userId);

        return favoriteProducts.stream()
                .map(favorite -> {
                    ProductEntity product = favorite.getProduct();
                    List<String> imageUrls = productImageService.getProductImageByProductId(product.getId())
                            .stream()
                            .map(ProductImageEntity::getImageUrl)
                            .collect(Collectors.toList());

                    return new ProductResponseDto(
                            product.getId(),
                            product.getTitle(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getCategory() != null ? product.getCategory().getId() : null,
                            product.getUserId(),
                            product.getRegionId(),
                            product.getStatus(),
                            imageUrls
                    );
                })
                .collect(Collectors.toList());
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

    public List<ProductEntity> getProductsSortedByCreatedAtAndUpdatedAt() {
        return productRepository.findAllByOrderByCreatedAtDescUpdatedAtDesc();
    }

    public List<ProductEntity> getProductsSortedByCreatedAt() {
        return productRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<ProductEntity> getProductsSortedByUpdatedAt() {
        return productRepository.findAllByOrderByUpdatedAtDesc();
    }


    public List<ProductEntity> getProductsByPriceRangeAndSort(int minPrice, int maxPrice) {
        List<ProductEntity> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return products.stream()
                .sorted(Comparator.comparing(ProductEntity::getCreatedAt)
                        .thenComparing(ProductEntity::getUpdatedAt)
                        .reversed())
                .collect(Collectors.toList());
    }

    public List<ProductEntity> getProductsByPriceRange(int minPrice, int maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<ProductEntity> getProductsByPriceRangeAndSortCustom(int minPrice, int maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
}