package com.carrotzmarket.api.domain.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryDto {
    private Long id;
    private Long buyerId;
    private Long sellerId;
    private String title;
    private int price;
    private LocalDate transactionDate;
    private String tradingPlace;
    private String status;
}
