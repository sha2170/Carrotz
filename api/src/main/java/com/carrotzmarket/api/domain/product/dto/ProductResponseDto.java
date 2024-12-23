package com.carrotzmarket.api.domain.product.dto;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
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
        this(product); // 기존 생성자 호출
        this.imageUrls = imageUrls;
    }
}
