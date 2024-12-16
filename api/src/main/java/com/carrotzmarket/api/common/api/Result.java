package com.carrotzmarket.api.common.api;

import com.carrotzmarket.api.common.error.ErrorCodeInterface;
import com.carrotzmarket.api.common.error.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    private Integer resultCode; // 응답 코드
    private String resultMessage; // 응답 메시지
    private String resultDescription; // 응답 설명

    public static Result OK() {
        return Result.builder()
                .resultCode(200)
                .resultMessage("OK")
                .resultDescription("성공")
                .build();
    }

    public static Result ERROR(ErrorCodeInterface errorCodeInterface) {
        return Result.builder()
                .resultCode(errorCodeInterface.getErrorCode())
                .resultMessage(errorCodeInterface.getDescription())
                .resultDescription("오류")
                .build();
    }

    public static Result ERROR(ErrorCodeInterface errorCodeInterface, Throwable tx) {
        return Result.builder()
                .resultCode(errorCodeInterface.getErrorCode())
                .resultMessage(errorCodeInterface.getDescription())
                .resultDescription(tx.getLocalizedMessage())
                .build();
    }

    public static Result ERROR(ErrorCodeInterface errorCodeInterface, String description) {
        return Result.builder()
                .resultCode(errorCodeInterface.getErrorCode())
                .resultMessage(errorCodeInterface.getDescription())
                .resultDescription(description)
                .build();
    }
}
