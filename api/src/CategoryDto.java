package com.example.category_service.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryDto는 클라이언트에 전달할 카테고리 데이터를 캡슐화하는 클래스입니다.
 * - Entity 클래스를 직접 노출하지 않고 필요한 데이터만 전달
 * - 계층적 구조를 지원하기 위해 children 필드 포함
 */
public class CategoryDto {

    private Long id; // 카테고리 ID
    private String name; // 카테고리 이름
    private String description; // 카테고리 설명
    private boolean enabled; // 카테고리 활성화 여부
    private List<CategoryDto> children = new ArrayList<>(); // 하위 카테고리 목록

    public CategoryDto(Long id, String name, String description, boolean enabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }

    public CategoryDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public List<CategoryDto> getChildren() { return children; }
    public void setChildren(List<CategoryDto> children) { this.children = children; }
}
