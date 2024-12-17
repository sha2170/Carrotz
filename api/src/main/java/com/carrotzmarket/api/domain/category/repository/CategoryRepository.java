package com.carrotzmarket.api.domain.category.repository;

import com.carrotzmarket.api.domain.category.domain.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CategoryRepository
 * - 카테고리 관련 데이터베이스 작업을 처리합니다.
 * - EntityManager를 통해 JPA 표준 API를 사용합니다.
 */
@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 모든 카테고리를 조회하며, 부모-자식 관계를 한 번에 로딩합니다.
     * @return 카테고리 목록
     */
    public List<Category> findAll() {
        return entityManager.createQuery(
                        "SELECT c FROM Category c LEFT JOIN FETCH c.children", Category.class)
                .getResultList();
    }

    /**
     * ID로 카테고리를 조회합니다.
     * @param id 조회할 카테고리의 ID
     * @return Category 엔티티
     */
    public Category findById(Long id) {
        return entityManager.find(Category.class, id);
    }

    /**
     * 키워드를 포함하는 카테고리를 조회합니다.
     * @param keyword 검색 키워드
     * @return 카테고리 목록
     */
    public List<Category> findByNameContainingIgnoreCase(String keyword) {
        TypedQuery<Category> query = entityManager.createQuery(
                "SELECT c FROM Category c WHERE LOWER(c.name) LIKE :keyword", Category.class);
        query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        return query.getResultList();
    }

    /**
     * 카테고리를 저장 또는 수정합니다.
     * @param category 저장할 카테고리 엔티티
     */
    @Transactional
    public void save(Category category) {
        if (category.getId() == null) {
            entityManager.persist(category);
        } else {
            entityManager.merge(category);
        }
    }

    /**
     * 카테고리를 삭제합니다.
     * @param category 삭제할 카테고리 엔티티
     */
    @Transactional
    public void delete(Category category) {
        entityManager.remove(entityManager.contains(category) ? category : entityManager.merge(category));
    }
}
