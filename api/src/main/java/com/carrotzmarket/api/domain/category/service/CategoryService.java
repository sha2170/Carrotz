package com.carrotzmarket.api.domain.category.service;

import com.carrotzmarket.api.domain.category.domain.Category;
import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * CategoryService
 * - 비즈니스 로직을 처리합니다.
 * - 카테고리 계층 구조를 생성하고, 특정 카테고리 또는 검색 기능을 제공합니다.
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 모든 카테고리를 계층 구조로 조회합니다.
     * @return 계층 구조의 카테고리 목록
     */
    public List<CategoryDto> getAllCategories() {
        return buildCategoryTree(categoryRepository.findAll());
    }

    /**
     * 특정 카테고리와 하위 카테고리를 조회합니다.
     * @param id 카테고리 ID
     * @return 카테고리 정보
     */
    public CategoryDto getCategoryWithSubcategories(Long id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new NoSuchElementException("Category with ID " + id + " not found");
        }
        return convertToDto(category);
    }

    /**
     * 키워드를 포함하는 카테고리를 검색합니다.
     * @param keyword 검색 키워드
     * @return 검색된 카테고리 목록
     */
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
