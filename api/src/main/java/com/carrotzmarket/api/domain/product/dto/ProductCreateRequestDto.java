package com.carrotzmarket.api.domain.product.dto;

import com.carrotzmarket.db.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {

    // 필수 입력
    private String title;
    private String description;
    private int price;

    private Long userId;
    private Long regionId;
    private ProductStatus status;
}
