package com.carrotzmarket.db.transaction_review.entity;

import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private ProductTransactionEntity transaction;

    @Column(name = "reviewer_id", nullable = false)
    private Long reviewerId;

    @Column(nullable = false)
    private String content;

    @Column(name = "score", nullable = false)
    private int carrotScore;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
