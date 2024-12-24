package com.carrotzmarket.api.domain.transaction_review.controller;

import com.carrotzmarket.api.domain.transaction_review.dto.ReviewRequestDto;
import com.carrotzmarket.api.domain.transaction_review.service.TransactionReviewService;
import com.carrotzmarket.db.transaction_review.entity.TransactionReviewEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionReviewController {

    private final TransactionReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<String> createReview(@RequestBody ReviewRequestDto request) {
        reviewService.createReview(request);
        return ResponseEntity.ok("리뷰 작성이 완료되었습니다!");
    }
}

