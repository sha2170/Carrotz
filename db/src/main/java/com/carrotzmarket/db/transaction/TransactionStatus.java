package com.carrotzmarket.db.transaction;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    IN_PROGRESS, RESERVED ,COMPLETED, CANCELED;
}
