package com.carrotzmarket.api.domain.product.dto;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String title;
    private String description;
    private int price;
    private Long userId;
    private Long regionId;
    private CategoryDto category;
    private ProductStatus status;
    private List<String> imageUrls;
    private int favoriteCount;
    private int viewCount;
    private boolean isViewed;

    // 기본 생성자
    public ProductResponseDto(ProductEntity product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.userId = product.getUserId();
        this.regionId = product.getRegionId();
        this.status = product.getStatus();
        this.favoriteCount = product.getFavoriteCount();
        this.viewCount = product.getViewCount();
        this.isViewed = false; // 기본값

        if (product.getCategory() != null) {
            this.category = new CategoryDto(
                    product.getCategory().getId(),
                    product.getCategory().getName(),
                    product.getCategory().getDescription(),
                    product.getCategory().isEnabled()
            );
        }
    }

    public ProductResponseDto(ProductEntity product, List<String> imageUrls) {
        this(product);
        this.imageUrls = imageUrls;
    }

    public ProductResponseDto(ProductEntity product, List<String> imageUrls, boolean isViewed) {
        this(product, imageUrls);
        this.isViewed = isViewed;
    }

    public ProductResponseDto(Long id, String title, String description, int price, Long userId, Long regionId,
                              CategoryDto category, ProductStatus status, List<String> imageUrls,
                              int favoriteCount, int viewCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.userId = userId;
        this.regionId = regionId;
        this.category = category;
        this.status = status;
        this.imageUrls = imageUrls;
        this.favoriteCount = favoriteCount;
        this.viewCount = viewCount;
        this.isViewed = false;
    }
}
