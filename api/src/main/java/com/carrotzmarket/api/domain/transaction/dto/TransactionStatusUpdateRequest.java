package com.carrotzmarket.api.domain.transaction.dto;

import com.carrotzmarket.db.transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionStatusUpdateRequest {
    private Long productId;
    private Long authorId;
    private TransactionStatus status;

}
