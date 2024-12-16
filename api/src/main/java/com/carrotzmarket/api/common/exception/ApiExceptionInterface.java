package com.carrotzmarket.api.common.exception;

import com.carrotzmarket.api.common.error.ErrorCodeInterface;

public interface ApiExceptionInterface { // 인터페이스는 규칙을 정한다!

    ErrorCodeInterface getErrorCodeInterface();

    String getErrorDescription();
}
