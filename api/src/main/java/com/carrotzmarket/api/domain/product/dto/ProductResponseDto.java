package com.carrotzmarket.api.domain.product.dto;

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
    private Long categoryId;
    private Long userId;
    private Long regionId;
    private ProductStatus status;
    private List<String> imageUrls;


}
