package com.carrotzmarket.api.domain.category.controller;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.api.domain.category.service.CategoryService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }


    @GetMapping("/search/{name}")
    public ResponseEntity<List<CategoryDto>> searchCategoriesByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getCategoriesByNameContaining(name));
    }

}
