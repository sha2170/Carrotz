package com.carrotzmarket.api.domain.category.repository;

import com.carrotzmarket.api.domain.category.domain.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository 클래스입니다.
 * - EntityManager를 통해 데이터베이스 작업 수행
 * - Spring Data JPA 대신 JPA 표준 API를 활용
 */
@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Category> findAll() {
        return entityManager.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    public Category findById(Long id) {
        return entityManager.find(Category.class, id);
    }

    public List<Category> findAllEnabledCategories() {
        return entityManager.createQuery("SELECT c FROM Category c WHERE c.enabled = true", Category.class)
                .getResultList();
    }

    public List<Category> findByNameContainingIgnoreCase(String keyword) {
        TypedQuery<Category> query = entityManager.createQuery(
                "SELECT c FROM Category c WHERE LOWER(c.name) LIKE :keyword", Category.class);
        query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        return query.getResultList();
    }

    public void save(Category category) {
        if (category.getId() == null) {
            entityManager.persist(category);
        } else {
            entityManager.merge(category);
        }
    }

    public void delete(Category category) {
        entityManager.remove(entityManager.contains(category) ? category : entityManager.merge(category));
    }
}
