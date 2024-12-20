package com.carrotzmarket.api.domain.category.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryDto {

    private Long id;
    private String name;
    private String description;
    private boolean enabled;
    private List<CategoryDto> children = new ArrayList<>();  // 자식 카테고리 리스트 추가

    public CategoryDto(Long id, String name, String description, boolean enabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }

    public CategoryDto() {
        this.children = new ArrayList<>();
    }
}
