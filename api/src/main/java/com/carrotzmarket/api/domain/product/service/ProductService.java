package com.carrotzmarket.api.domain.product.service;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import com.carrotzmarket.api.domain.favoriteProduct.repository.FavoriteProductRepository;
import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.dto.ProductResponseDto;
import com.carrotzmarket.api.domain.product.dto.ProductUpdateRequestDto;
import com.carrotzmarket.api.domain.product.repository.ProductRepository;
import com.carrotzmarket.api.domain.productImage.service.FileUploadService;
import com.carrotzmarket.api.domain.productImage.service.ProductImageService;
import com.carrotzmarket.api.domain.region.service.RegionService;
import com.carrotzmarket.api.domain.transaction.repository.ProductTransactionRepository;
import com.carrotzmarket.api.domain.user.dto.ProductSummaryDto;
import com.carrotzmarket.api.domain.user.dto.SellerProfileDto;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.api.domain.user.service.UserService;
import com.carrotzmarket.api.domain.viewedProduct.service.ViewedProductService;
import com.carrotzmarket.db.category.CategoryEntity;
import com.carrotzmarket.db.favoriteProduct.FavoriteProductEntity;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import com.carrotzmarket.db.productImage.ProductImageEntity;
import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import com.carrotzmarket.db.transaction.TransactionStatus;
import com.carrotzmarket.db.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private final UserRepository userRepository;
    private final ViewedProductService viewedProductService;
    private final ProductTransactionRepository productTransactionRepository;
    private final UserService userService;

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


    public void deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        productRepository.delete(product);
    }


    private void saveProductImages(Long productId, List<MultipartFile> images) {
        List<ProductImageEntity> productImages = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                String imageUrl = fileUploadService.uploadFile(image);
                ProductImageEntity productImage = new ProductImageEntity();
                productImage.setProductId(productId);
                productImage.setImageUrl(imageUrl);
                productImages.add(productImage);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + image.getOriginalFilename(), e);
            }
        }
        productImageService.saveAll(productImages);
    }


    public ProductResponseDto getProductById(Long id) {
        incrementViewCount(id);

        ProductEntity product = findProductById(id);

        List<ProductImageEntity> productImages = productImageService.getProductImageByProductId(id);
        List<String> imageUrls = productImages.stream()
                .map(ProductImageEntity::getImageUrl)
                .collect(Collectors.toList());

        return new ProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getUserId(),
                product.getRegionId(),
                product.getCategory() != null ? new CategoryDto(
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getCategory().getDescription(),
                        product.getCategory().isEnabled()
                ) : null,
                product.getStatus(),
                imageUrls,
                product.getFavoriteCount(),
                product.getViewCount()
        );
    }


    public String addFavoriteProduct(Long userId, Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다."));

        Optional<FavoriteProductEntity> existingFavorite = favoriteProductRepository.findByUserIdAndProductId(userId, productId);
        if (existingFavorite.isPresent()) {
            return "이미 관심 상품으로 등록되어 있습니다.";
        }

        FavoriteProductEntity favorite = FavoriteProductEntity.builder()
                .userId(userId)
                .product(product)
                .build();
        favoriteProductRepository.save(favorite);

        product.setFavoriteCount(product.getFavoriteCount() + 1);
        productRepository.save(product);

        return "관심 상품으로 등록되었습니다.";
    }


    public String removeFavoriteProduct(Long userId, Long productId) {
        Optional<FavoriteProductEntity> existingFavorite =
                favoriteProductRepository.findByUserIdAndProductId(userId, productId);

        if (existingFavorite.isEmpty()) {
            return "해당 상품은 관심 상품으로 등록되어 있지 않습니다.";
        }

        favoriteProductRepository.delete(existingFavorite.get());

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        product.setFavoriteCount(Math.max(product.getFavoriteCount() - 1, 0)); // 최소값 0 유지
        productRepository.save(product);

        return "관심 상품이 정상적으로 해제되었습니다.";
    }


    public List<Object> getFavoriteProductsByUserId(Long userId) {
        List<FavoriteProductEntity> favoriteProducts = favoriteProductRepository.findByUserId(userId);

        if (favoriteProducts.isEmpty()) {
            return List.of("해당 유저의 관심 상품이 등록되어 있지 않습니다.");
        }

        return favoriteProducts.stream()
                .map(favorite -> {
                    ProductEntity product = favorite.getProduct();

                    if (product == null) {
                        return "해당 상품은 등록되어 있지 않은 상품입니다.";
                    }

                    List<String> imageUrls = productImageService.getProductImageByProductId(product.getId())
                            .stream()
                            .map(ProductImageEntity::getImageUrl)
                            .collect(Collectors.toList());

                    return new ProductResponseDto(product, imageUrls);
                })
                .collect(Collectors.toList());
    }



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
                product.getStatus(),
                imageUrls,
                product.getFavoriteCount(),
                product.getViewCount()
        );
    }


    public ProductResponseDto updateProductStatus(Long id, ProductStatus status) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        product.setStatus(status);
        productRepository.save(product);

        List<String> imageUrls = productImageService.getProductImageByProductId(id)
                .stream()
                .map(ProductImageEntity::getImageUrl)
                .collect(Collectors.toList());

        return new ProductResponseDto(product, imageUrls);
    }

    public Map<String, Object> getSellerMannerTemperature(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        Long sellerId = product.getUserId();
        Double mannerTemperature = userRepository.findMannerTemperatureById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 판매자를 찾을 수 없습니다."));

        Map<String, Object> response = new HashMap<>();
        response.put("sellerId", sellerId);
        response.put("mannerTemperature", mannerTemperature);

        return response;
    }

    @Transactional
    public void incrementViewCount(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
    }

    @Transactional
    public Map<String, Object> getSellerInfoAndOtherProducts(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Long sellerId = product.getUserId();
        UserEntity seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다."));

        SellerProfileDto sellerProfile = new SellerProfileDto(
                seller.getId(),
                seller.getLoginId(),
                seller.getProfileImageUrl()
        );

        List<ProductEntity> otherProducts = productRepository.findByUserId(sellerId)
                .stream()
                .filter(p -> !p.getId().equals(productId))
                .collect(Collectors.toList());

        List<ProductSummaryDto> otherProductDtos = otherProducts.stream()
                .map(p -> new ProductSummaryDto(p.getId(), p.getTitle(), p.getPrice()))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("sellerProfile", sellerProfile);
        result.put("otherProducts", otherProductDtos);

        return result;
    }

    @Transactional
    public void updateTransactionStatus(Long transactionId, String status) {
        ProductTransactionEntity transaction = productTransactionRepository.findTransactionDetailById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        if ("COMPLETED".equals(status) && !"COMPLETED".equals(transaction.getStatus())) {
            transaction.setStatus(TransactionStatus.COMPLETED);
            productTransactionRepository.save(transaction);

            UserEntity seller = userRepository.findById(transaction.getSellerId())
                    .orElseThrow(() -> new IllegalArgumentException("Seller not found"));
            seller.setMannerTemperature(seller.getMannerTemperature() + 1);
            userRepository.save(seller);
        }
    }

    @Transactional
    public void completeTransaction(Long transactionId) {
        // 거래 상태 업데이트
        ProductTransactionEntity transaction = productTransactionRepository.findTransactionDetailById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("거래를 찾을 수 없습니다."));
        transaction.setStatus(TransactionStatus.COMPLETED);
        productTransactionRepository.save(transaction);

        // 판매자의 매너 온도 업데이트
        userService.updateMannerTemperature(transaction.getSellerId());
    }

    public ProductResponseDto getProductById(Long id, Long userId) {
        viewedProductService.recordViewedProduct(userId, id);

        ProductEntity product = findProductById(id);

        List<ProductImageEntity> productImages = productImageService.getProductImageByProductId(id);
        List<String> imageUrls = productImages.stream()
                .map(ProductImageEntity::getImageUrl)
                .collect(Collectors.toList());

        boolean isViewed = viewedProductService.getViewedProductIds(userId).contains(id);

        return new ProductResponseDto(
                product,
                imageUrls,
                isViewed
        );
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


    public List<ProductResponseDto> getSearchResults(Long userId, String title) {
        List<ProductEntity> products = productRepository.findByTitleContaining(title);

        Set<Long> viewedProductIds = viewedProductService.getViewedProductIds(userId);

        return products.stream()
                .map(product -> new ProductResponseDto(
                        product,
                        productImageService.getProductImageByProductId(product.getId()).stream()
                                .map(ProductImageEntity::getImageUrl)
                                .collect(Collectors.toList()),
                        viewedProductIds.contains(product.getId())
                ))
                .collect(Collectors.toList());
    }
}
