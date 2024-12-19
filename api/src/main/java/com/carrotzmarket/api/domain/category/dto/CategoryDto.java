package com.carrotzmarket.api.domain.category.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * CategoryDto
 * - 데이터 전달 객체 (DTO)로, 클라이언트에 반환할 카테고리 데이터를 담는 클래스입니다.
 * - 계층적 구조(부모-자식 관계)를 위해 children 필드를 포함합니다.
 */
@Getter @Setter
public class CategoryDto {

    private Long id;
    private String name;
    private String description;
    private boolean enabled;
    private List<CategoryDto> children;

    public CategoryDto(Long id, String name, String description, boolean enabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.children = new ArrayList<>();
    }

    public CategoryDto() {
        this.children = new ArrayList<>();
    }

}
