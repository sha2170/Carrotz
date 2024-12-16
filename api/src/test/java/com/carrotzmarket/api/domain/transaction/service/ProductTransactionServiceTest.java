package com.carrotzmarket.api.domain.transaction.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.carrotzmarket.api.domain.transaction.converter.ProductTransactionConverter;
import com.carrotzmarket.api.domain.transaction.dto.PurchaseRequest;
import com.carrotzmarket.api.domain.transaction.repository.ProductTransactionRepository;
import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import com.carrotzmarket.db.transaction.TransactionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductTransactionServiceTest {

    @Mock
    ProductTransactionRepository repository;

    @Mock
    ProductTransactionConverter converter;

    @InjectMocks
    ProductTransactionService service;

    @Test
    void 사용자가_구매요청을_보내면_거래내역이_생성된다() {
        // given
        PurchaseRequest request = createRequest();
        ProductTransactionEntity mockEntity = createEntity(request);

        when(converter.toEntity(any(PurchaseRequest.class))).thenReturn(mockEntity);
        when(repository.save(any(ProductTransactionEntity.class))).thenReturn(mockEntity);

        // when
        ProductTransactionEntity result = service.createTransaction(request);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBuyerId()).isEqualTo(mockEntity.getBuyerId());
        Assertions.assertThat(result.getSellerId()).isEqualTo(mockEntity.getSellerId());
        Assertions.assertThat(result.getProductId()).isEqualTo(mockEntity.getProductId());
        Assertions.assertThat(result.getTransactionDate()).isEqualTo(mockEntity.getTransactionDate());
        Assertions.assertThat(result.getTradingPlace()).isEqualTo(mockEntity.getTradingPlace());
        Assertions.assertThat(result.getStatus()).isEqualTo(mockEntity.getStatus());
    }

    private static ProductTransactionEntity createEntity(PurchaseRequest request) {
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
    }

    private static PurchaseRequest createRequest() {
        PurchaseRequest request = new PurchaseRequest();
        request.setProductId(1L);
        request.setSellerId(1L);
        request.setBuyerId(2L);
        request.setTransactionDate(LocalDate.now());
        request.setTradingHours(LocalDateTime.now());
        request.setTradingPlace("서울시");
        return request;
    }
}