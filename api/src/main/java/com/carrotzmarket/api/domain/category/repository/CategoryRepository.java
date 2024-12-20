package com.carrotzmarket.api.domain.category.repository;

import com.carrotzmarket.db.category.CategoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryEntity> findAll() {
        return entityManager.createQuery(
                        "SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.children", CategoryEntity.class)
                .getResultList();
    }

    public Optional<CategoryEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(CategoryEntity.class, id));
    }

    public List<CategoryEntity> findByNameContaining(String name) {
        return entityManager.createQuery(
                        "SELECT c FROM CategoryEntity c WHERE c.name LIKE :name", CategoryEntity.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }


    @Transactional
    public CategoryEntity save(CategoryEntity category) {
        if (category.getId() == null) {
            entityManager.persist(category);
            return category;
        } else {
            return entityManager.merge(category);
        }
    }
}
