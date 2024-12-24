package com.carrotzmarket.api.domain.transaction_review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {
    private Long transactionId;
    private Long reviewerId;
    private int carrotScore;
    private String content;
}
