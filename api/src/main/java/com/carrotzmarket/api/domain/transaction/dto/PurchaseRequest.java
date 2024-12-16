package com.carrotzmarket.api.domain.transaction.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PurchaseRequest {
    private Long productId;
    private Long buyerId;
    private Long sellerId;

    private LocalDate transactionDate;
    private LocalDateTime tradingHours;
    private String tradingPlace;
}
