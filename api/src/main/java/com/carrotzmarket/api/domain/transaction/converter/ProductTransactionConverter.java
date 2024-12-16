package com.carrotzmarket.api.domain.transaction.converter;

import com.carrotzmarket.api.domain.transaction.dto.PurchaseRequest;
import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import com.carrotzmarket.db.transaction.TransactionStatus;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductTransactionConverter {
    public ProductTransactionEntity toEntity(PurchaseRequest request) {
        return Optional.ofNullable(request)
                .map(it -> {
                    return ProductTransactionEntity.builder()
                            .buyerId(request.getBuyerId())
                            .sellerId(request.getSellerId())
                            .productId(request.getProductId())
                            .transactionDate(request.getTransactionDate())
                            .tradingHours(request.getTradingHours())
                            .tradingPlace(request.getTradingPlace())
                            .status(TransactionStatus.IN_PROGRESS)
                            .hasReview(false)
                            .build();
                }).orElseThrow(() -> new IllegalStateException("예외 발생"));
    }
}
