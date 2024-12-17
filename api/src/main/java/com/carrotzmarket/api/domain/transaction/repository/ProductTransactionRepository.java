package com.carrotzmarket.api.domain.transaction.repository;

import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductTransactionRepository {

    private final EntityManager em;

    public ProductTransactionEntity save(ProductTransactionEntity productTransaction) {
        em.persist(productTransaction);
        return productTransaction;
    }

    public List<ProductTransactionEntity> findAllPurchaseHistoryByUserId(Long id) {
        return em.createQuery("SELECT P FROM ProductTransactionEntity P WHERE P.buyerId = :id AND P.status = 'COMPLETED'", ProductTransactionEntity.class)
                .setParameter("id", id)
                .getResultList();
    }
}
