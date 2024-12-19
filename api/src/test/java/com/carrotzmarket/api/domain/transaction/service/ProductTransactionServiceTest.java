package com.carrotzmarket.api.domain.transaction.service;


import static com.carrotzmarket.db.transaction.TransactionStatus.CANCELED;
import static com.carrotzmarket.db.transaction.TransactionStatus.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.carrotzmarket.api.domain.transaction.converter.ProductTransactionConverter;
import com.carrotzmarket.api.domain.transaction.dto.PurchaseRequest;
import com.carrotzmarket.api.domain.transaction.dto.TransactionStatusUpdateRequest;
import com.carrotzmarket.api.domain.transaction.repository.ProductTransactionRepository;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import com.carrotzmarket.db.transaction.TransactionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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
        Long buyerId = 1L;
        Long sellerId = 2L;
        Long productId = 3L;

        PurchaseRequest request = createRequest(buyerId);
        ProductTransactionEntity entity = createTransaction(buyerId, sellerId, productId);

        when(converter.toEntity(any(PurchaseRequest.class))).thenReturn(entity);
        when(repository.save(any(ProductTransactionEntity.class))).thenReturn(entity);

        // when
        ProductTransactionEntity result = service.createTransaction(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBuyerId()).isEqualTo(entity.getBuyerId());
        assertThat(result.getSellerId()).isEqualTo(entity.getSellerId());
        assertThat(result.getProduct()).isEqualTo(entity.getProduct());
        assertThat(result.getTransactionDate()).isEqualTo(entity.getTransactionDate());
        assertThat(result.getTradingPlace()).isEqualTo(entity.getTradingPlace());
        assertThat(result.getStatus()).isEqualTo(entity.getStatus());
    }

    @Test
    public void 거래내역_테이블을_기반으로_사용자의_ID를_통하여_거래가완료된_구매내역을_가져온다() {
        // given
        Long buyerId = 1L;
        Long sellerId = 2L;
        Long productId = 3L;

        ProductTransactionEntity entity1 = createTransaction(buyerId, sellerId, productId);
        ProductTransactionEntity entity2 = createTransaction(buyerId, sellerId + 1, productId + 1);

        entity1.setStatus(COMPLETED);
        entity2.setStatus(COMPLETED);

        when(repository.findAllPurchaseHistoryByUserId(buyerId)).thenReturn(List.of(entity1, entity2));

        // when
        List<ProductTransactionEntity> purchaseHistory = service.findAllPurchaseHistory(buyerId);

        // then
        assertThat(purchaseHistory)
                .isNotEmpty()
                .hasSize(2)
                .allSatisfy(entity -> {
                    assertThat(entity.getBuyerId()).isEqualTo(buyerId);
                    assertThat(entity.getStatus()).isEqualTo(COMPLETED);
                });

        verify(repository, times(1)).findAllPurchaseHistoryByUserId(buyerId);
    }

    @Test
    public void 거래내역_테이블에서_거래가_완료되지_않은_구매내역은_가져올수없다() {
        // give
        Long buyerId = 1L;
        Long sellerId = 2L;
        Long productId = 3L;

        ProductTransactionEntity entity1 = createTransaction(buyerId, sellerId, productId);
        ProductTransactionEntity entity2 = createTransaction(buyerId, sellerId + 1, productId + 1);

        entity1.setStatus(CANCELED);
        entity2.setStatus(COMPLETED);

        when(repository.findAllPurchaseHistoryByUserId(buyerId)).thenReturn(List.of(entity2));

        // when
        List<ProductTransactionEntity> purchaseHistory = service.findAllPurchaseHistory(buyerId);

        // then
        assertThat(purchaseHistory)
                .isNotEmpty()
                .hasSize(1);

        assertThat(purchaseHistory)
                .allSatisfy(entity -> {
                    assertThat(entity.getBuyerId()).isEqualTo(buyerId);
                    assertThat(entity.getStatus()).isEqualTo(COMPLETED);
                });

        verify(repository, times(1)).findAllPurchaseHistoryByUserId(buyerId);
    }

    @Test
    public void 거래내역_테이블을_기반으로_사용자의_ID를_통하여_거래가완료된_판매내역을_가져온다() {
        // given
        Long buyerId = 1L;
        Long sellerId = 2L;
        Long productId = 3L;

        ProductTransactionEntity entity1 = createTransaction(buyerId, sellerId, productId);
        ProductTransactionEntity entity2 = createTransaction(buyerId + 1, sellerId, productId + 1);

        entity1.setStatus(COMPLETED);
        entity2.setStatus(COMPLETED);

        when(repository.findAllSalesHistoryByUserId(sellerId)).thenReturn(List.of(entity1, entity2));

        // when
        List<ProductTransactionEntity> purchaseHistory = service.findAllSalesHistory(sellerId);

        // then
        assertThat(purchaseHistory)
                .isNotEmpty()
                .hasSize(2)
                .allSatisfy(entity -> {
                    assertThat(entity.getSellerId()).isEqualTo(sellerId);
                    assertThat(entity.getStatus()).isEqualTo(COMPLETED);
                });

        verify(repository, times(1)).findAllSalesHistoryByUserId(sellerId);
    }

    @Test
    public void 거래내역_테이블에서_거래가완료되지않은_판매내역은_가져올수없다() {
        // given
        Long buyerId = 1L;
        Long sellerId = 2L;
        Long productId = 3L;

        ProductTransactionEntity entity1 = createTransaction(buyerId, sellerId, productId);
        ProductTransactionEntity entity2 = createTransaction(buyerId + 1, sellerId, productId + 1);

        entity1.setStatus(CANCELED);
        entity2.setStatus(COMPLETED);

        when(repository.findAllSalesHistoryByUserId(sellerId)).thenReturn(List.of(entity2));

        // when
        List<ProductTransactionEntity> purchaseHistory = service.findAllSalesHistory(sellerId);

        // then
        assertThat(purchaseHistory)
                .isNotEmpty()
                .hasSize(1)
                .allSatisfy(entity -> {
                    assertThat(entity.getSellerId()).isEqualTo(sellerId);
                    assertThat(entity.getStatus()).isEqualTo(COMPLETED);
                });

        verify(repository, times(1)).findAllSalesHistoryByUserId(sellerId);
    }

    private static ProductTransactionEntity createTransaction(Long buyerId, Long sellerId, Long productId) {
        return ProductTransactionEntity.builder()
                .buyerId(buyerId)
                .sellerId(sellerId)
                .product(createProduct())
                .transactionDate(LocalDate.now())
                .tradingHours(LocalDateTime.now())
                .tradingPlace("서울시")
                .status(TransactionStatus.IN_PROGRESS)
                .hasReview(false)
                .build();
    }

    private static ProductEntity createProduct() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setUserId(1L);
        product.setRegionId(1L);
        product.setTitle("책상");
        product.setDescription("설명");
        product.setPrice(1000);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setViewCount(0);
        product.setFavoriteCount(0);
        product.setStatus(ProductStatus.ON_SALE); // 기본 상태 설정
        return product;
    }

    private static PurchaseRequest createRequest(Long buyerId) {
        PurchaseRequest request = new PurchaseRequest();
        request.setProductId(buyerId);
        request.setSellerId(2L);
        request.setBuyerId(3L);
        request.setTransactionDate(LocalDate.now());
        request.setTradingHours(LocalDateTime.now());
        request.setTradingPlace("서울시");
        return request;
    }
}