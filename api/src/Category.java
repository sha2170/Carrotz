package com.example.category_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Category 엔티티는 카테고리를 데이터베이스 테이블과 매핑합니다.
 * - 부모-자식 관계를 설정하여 계층 구조를 지원
 * - 활성화 여부(enabled)를 통해 필터링 가능
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성
    private Long id;

    @Column(nullable = false)
    @NotBlank // 이름은 비어 있을 수 없음
    private String name;

    @Column
    private String description; // 카테고리 설명

    @ManyToOne
    @JoinColumn(name = "parent_id") // 부모 카테고리와의 관계
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>(); // 하위 카테고리 목록

    @Column(nullable = false)
    private boolean enabled; // 활성화 여부

    public Category() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getParent() { return parent; }
    public void setParent(Category parent) { this.parent = parent; }

    public List<Category> getChildren() { return children; }
    public void setChildren(List<Category> children) { this.children = children; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
