package com.carrotzmarket.api.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    // 필수 입력
    private String title;
    private String description;
    private int price;

    // private Long user
}