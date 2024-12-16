package com.carrotzmarket.api.domain.transaction.repository;

import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import jakarta.persistence.EntityManager;
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
}
