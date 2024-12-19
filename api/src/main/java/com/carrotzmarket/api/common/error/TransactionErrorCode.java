package com.carrotzmarket.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionErrorCode implements ErrorCodeInterface {
    TRANSACTION_NOT_FOUND(400, 1500, "해당 거래정보를 찾을 수 없습니다."),
    INVALID_TRANSACTION_STATUS_CHANGE(400, 1501, "거래가 완료되었거나 취소된 거래의 상태는 변경할 수 없습니다"),
    DUPLICATE_TRANSACTION_STATUS_CHANGE(400, 1502, "변경하려는 상태와 현재 상태가 동일합니다."),
    ONLY_SELLER_CAN_CHANGE_STATUS(400, 1503, "상품의 상태는 판매자만 변경할 수 있습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
