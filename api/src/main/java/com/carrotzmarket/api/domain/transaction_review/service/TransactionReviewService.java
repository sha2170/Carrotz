package com.carrotzmarket.api.domain.transaction_review.service;

import com.carrotzmarket.api.domain.transaction_review.dto.ReviewRequestDto;
import com.carrotzmarket.api.domain.transaction_review.repository.TransactionReviewRepository;
import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import com.carrotzmarket.db.transaction.TransactionStatus;
import com.carrotzmarket.db.transaction_review.entity.TransactionReviewEntity;
import com.carrotzmarket.api.domain.transaction.repository.ProductTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionReviewService {

    private final ProductTransactionRepository transactionRepository;
    private final TransactionReviewRepository reviewRepository;

    @Transactional
    public TransactionReviewEntity createReview(ReviewRequestDto request) {
        ProductTransactionEntity transaction = transactionRepository.findTransactionDetailById(request.getTransactionId())
                .orElseThrow(() -> new IllegalStateException("Transaction not found with id: " + request.getTransactionId()));

        if (!TransactionStatus.COMPLETED.equals(transaction.getStatus())) {
            throw new IllegalStateException("거래가 완료된 상태에서만 리뷰를 작성할 수 있습니다.");
        }

        if (Boolean.TRUE.equals(transaction.getHasReview())) {
            throw new IllegalStateException("이미 작성된 리뷰가 있습니다.");
        }

        if (!transaction.getBuyerId().equals(request.getReviewerId())) {
            throw new IllegalStateException("해당 상품을 구매한 사람만 리뷰를 작성할 수 있습니다.");
        }

        TransactionReviewEntity review = TransactionReviewEntity.builder()
                .transaction(transaction)
                .reviewerId(transaction.getBuyerId())
                .content(request.getContent())
                .carrotScore(request.getCarrotScore())
                .build();

        transaction.setHasReview(true);
        transactionRepository.save(transaction);

        return reviewRepository.save(review);
    }
}
