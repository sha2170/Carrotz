package com.carrotzmarket.api.domain.category.service;

import com.carrotzmarket.api.domain.category.domain.Category;
import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * CategoryService는 비즈니스 로직을 처리합니다.
 * - 계층 구조로 데이터를 반환
 * - 특정 카테고리 및 하위 카테고리 조회
 * - 키워드 기반 검색
 */
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

    public CategoryDto getCategoryWithSubcategories(Long id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new NoSuchElementException("Category not found");
        }
        return convertToDto(category);
    }

    public List<CategoryDto> searchCategories(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isEnabled()
        );
    }

    private List<CategoryDto> buildCategoryTree(List<Category> categories) {
        Map<Long, CategoryDto> map = new HashMap<>();
        categories.forEach(category -> map.put(category.getId(), convertToDto(category)));

        List<CategoryDto> roots = new ArrayList<>();
        for (Category category : categories) {
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
