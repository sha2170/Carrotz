package com.carrotzmarket.db.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_transaction")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ProductTransactionEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long sellerId;

    private Long buyerId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Boolean hasReview;

    private LocalDate transactionDate;
    private LocalDateTime tradingHours;
    private String tradingPlace;
}
