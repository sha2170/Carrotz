package com.carrotzmarket.api.domain.category.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryDto
 * - 데이터 전달 객체 (DTO)로, 클라이언트에 반환할 카테고리 데이터를 담는 클래스입니다.
 * - 계층적 구조(부모-자식 관계)를 위해 children 필드를 포함합니다.
 */
public class CategoryDto {

    private Long id;                 // 카테고리 ID
    private String name;             // 카테고리 이름
    private String description;      // 카테고리 설명
    private boolean enabled;         // 카테고리 활성화 여부
    private List<CategoryDto> children; // 하위 카테고리 목록

    /**
     * 모든 필드를 초기화하는 생성자
     * @param id 카테고리 ID
     * @param name 카테고리 이름
     * @param description 카테고리 설명
     * @param enabled 활성화 여부
     */
    public CategoryDto(Long id, String name, String description, boolean enabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.children = new ArrayList<>(); // children 초기화 (NPE 방지)
    }

    /**
     * 기본 생성자
     * children 필드를 초기화해 NullPointerException을 방지합니다.
     */
    public CategoryDto() {
        this.children = new ArrayList<>();
    }

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
