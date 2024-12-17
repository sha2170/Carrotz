package com.example.category_service.controller;

import com.example.category_service.dto.CategoryDto;
import com.example.category_service.service.CategoryService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CategoryController는 클라이언트의 요청을 처리하고 응답을 반환하는 역할을 합니다.
 * - 카테고리의 계층 구조 조회
 * - 특정 카테고리 조회
 * - 키워드 기반 검색 기능 제공
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 전체 카테고리 트리 조회
     * @return 계층적 구조의 모든 카테고리 목록
     */
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * 특정 카테고리 및 하위 카테고리 조회
     * @param id 조회할 카테고리의 ID
     * @return 해당 카테고리와 하위 카테고리 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryWithSubcategories(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryWithSubcategories(id));
    }

    /**
     * 카테고리 이름을 기반으로 키워드 검색
     * @param keyword 검색할 키워드
     * @return 키워드와 일치하는 카테고리 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDto>> searchCategories(@RequestParam @NotBlank String keyword) {
        return ResponseEntity.ok(categoryService.searchCategories(keyword));
    }
}
