package com.carrotzmarket.api.domain.category.service;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import com.carrotzmarket.db.category.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return buildCategoryTree(categoryRepository.findAll());
    }

    public CategoryDto getCategoryById(Long id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category not found with ID: " + id);
        }
        return convertToDto(categoryOptional.get());
    }

    public List<CategoryDto> getCategoriesByNameContaining(String name) {
        List<CategoryEntity> categories = categoryRepository.findByNameContaining(name);
        return categories.stream()
                .map(this::convertToDto) // 카테고리 엔티티를 DTO로 변환
                .toList();
    }

    private CategoryDto convertToDto(CategoryEntity category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isEnabled()
        );
    }

    private List<CategoryDto> buildCategoryTree(List<CategoryEntity> categories) {
        Map<Long, CategoryDto> map = new HashMap<>();
        categories.forEach(category -> map.put(category.getId(), convertToDto(category)));

        List<CategoryDto> roots = new ArrayList<>();
        for (CategoryEntity category : categories) {
            CategoryDto dto = map.get(category.getId());
            if (category.getParent() != null) {
                map.get(category.getParent().getId()).getChildren().add(dto);
            } else {
                roots.add(dto);
            }
        }
        return roots;
    }
}
