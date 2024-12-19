package com.carrotzmarket.api.domain.category.controller;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.api.domain.category.service.CategoryService;
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

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryWithSubcategories(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryWithSubcategories(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryDto>> searchCategories(@RequestParam @NotBlank String keyword) {
        return ResponseEntity.ok(categoryService.searchCategories(keyword));
    }
}
