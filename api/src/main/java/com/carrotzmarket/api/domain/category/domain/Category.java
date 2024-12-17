package com.carrotzmarket.api.domain.category.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Category 엔티티
 * - 카테고리 정보를 데이터베이스에 저장하는 엔티티 클래스입니다.
 * - 계층 구조를 지원하기 위해 부모-자식 관계를 설정합니다.
 * - 직렬화 시 무한 순환 방지를 위해 @JsonManagedReference와 @JsonBackReference를 사용합니다.
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성
    private Long id;

    @Column(nullable = false)
    @NotBlank // 카테고리 이름은 비어 있을 수 없습니다.
    private String name;

    @Column
    private String description; // 카테고리 설명

    /**
     * 부모 카테고리와의 연관관계 설정
     * - @ManyToOne: 여러 자식 카테고리가 하나의 부모 카테고리를 가질 수 있습니다.
     * - @JsonBackReference: 직렬화 시 무한 루프를 방지합니다.
     */
    @ManyToOne
    @JoinColumn(name = "parent_id") // 부모 카테고리의 ID를 저장
    @JsonBackReference
    private Category parent;

    /**
     * 자식 카테고리와의 연관관계 설정
     * - @OneToMany: 하나의 부모 카테고리는 여러 자식 카테고리를 가질 수 있습니다.
     * - orphanRemoval = true: 부모-자식 관계가 끊어지면 자식 카테고리를 자동 삭제합니다.
     * - @JsonManagedReference: 자식 카테고리를 직렬화할 때만 포함합니다.
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Category> children = new ArrayList<>();

    @Column(nullable = false)
    private boolean enabled; // 카테고리 활성화 여부

    /**
     * 기본 생성자
     */
    public Category() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 자식 카테고리를 추가하는 편의 메서드
     * - 부모 관계를 설정하고 자식 리스트에 추가합니다.
     *
     * @param child 자식 카테고리
     */
    public void addChild(Category child) {
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * 자식 카테고리를 제거하는 편의 메서드
     * - 부모 관계를 해제하고 자식 리스트에서 제거합니다.
     *
     * @param child 자식 카테고리
     */
    public void removeChild(Category child) {
        child.setParent(null);
        this.children.remove(child);
    }
}
