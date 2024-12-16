package com.carrotzmarket.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 /* User의 경우 1000번대 에러코드 사용
 */
@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeInterface{
    // (HttpStatusCode, 우리서비스 에러코드, 코드 설명)
    USER_NOT_FOUND(400, 1404, "사용자를 찾을 수 없음"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}