package com.carrotzmarket.api.domain.transaction_review.repository;

import com.carrotzmarket.db.transaction_review.entity.TransactionReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionReviewRepository extends JpaRepository<TransactionReviewEntity, Long> {

    /**
     * 특정 거래 ID에 대한 리뷰 존재 여부 확인
     *
     * @param transactionId 거래 ID
     * @return Optional<TransactionReviewEntity>
     */
    @Query("SELECT r FROM TransactionReviewEntity r WHERE r.transaction.id = :transactionId")
    Optional<TransactionReviewEntity> findByTransactionId(@Param("transactionId") Long transactionId);

    /**
     * 특정 사용자가 작성한 리뷰 검색
     *
     * @param reviewerId 작성자 ID
     * @return Optional<TransactionReviewEntity>
     */
    @Query("SELECT r FROM TransactionReviewEntity r WHERE r.reviewerId = :reviewerId")
    Optional<TransactionReviewEntity> findByReviewerId(@Param("reviewerId") Long reviewerId);
}

