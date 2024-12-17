package com.carrotzmarket.api.domain.transaction.service;

import com.carrotzmarket.api.domain.transaction.converter.ProductTransactionConverter;
import com.carrotzmarket.api.domain.transaction.dto.PurchaseRequest;
import com.carrotzmarket.api.domain.transaction.repository.ProductTransactionRepository;
import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductTransactionService {

    private final ProductTransactionRepository repository;
    private final ProductTransactionConverter converter;

    /**
     * TODO : createTransaction() 구매자 ID, 판매자 ID, 상품 ID 유효성 검증
     * 구매자 ID, 판매자 ID, 상품 ID 각각 조회
     * 상품이 존재 하지 않거나, 유효하지 않는 사용자 ID가 요청된 경우 예외 발생
     */

    @Transactional(readOnly = true)
    public ProductTransactionEntity createTransaction(PurchaseRequest request) {
        ProductTransactionEntity entity = converter.toEntity(request);
        return repository.save(entity);
    }

    public List<ProductTransactionEntity> findAllPurchaseHistory(Long userId) {
        return repository.findAllPurchaseHistoryByUserId(userId);
    }

    public List<ProductTransactionEntity> findAllSalesHistory(Long userId) {
        return repository.findAllSalesHistoryByUserId(userId);
    }


}
