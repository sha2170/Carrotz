package com.carrotzmarket.api.domain.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 등록 요청을 위한 DTO 클래스
 * - 상품명, 설명, 가격, 사용자 ID, 지역 ID, 카테고리명을 입력받습니다.
 * - 카테고리명은 선택적으로 입력 가능합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "상품명은 필수 입력 항목입니다.")
    private String name;            // 상품명

    private String description;     // 상품 설명 (선택적)

    @NotNull(message = "가격은 필수 입력 항목입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;          // 상품 가격

    @NotNull(message = "사용자 ID는 필수 입력 항목입니다.")
    private Long userId;            // 사용자 ID

    @NotNull(message = "지역 ID는 필수 입력 항목입니다.")
    private Long regionId;          // 지역 ID

    private List<String> categoryNames;  // 여러 카테고리명을 받을 수 있도록 수정 (선택적 필드)
}
