package com.carrotzmarket.api.common.exception;

import com.carrotzmarket.api.common.error.ErrorCodeInterface;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionInterface { // 실행 중에 일어날 예외를 상속 받을 커스텀 익셉션 클래스

  private final ErrorCodeInterface errorCodeInterface;

  private final String errorDescription;

  public ApiException(ErrorCodeInterface errorCodeInterface) {
    super(errorCodeInterface.getDescription());
    this.errorCodeInterface = errorCodeInterface;
    this.errorDescription = errorCodeInterface.getDescription();
  }

  public ApiException(ErrorCodeInterface errorCodeInterface, String errorDescription) {
    super(errorDescription);
    this.errorCodeInterface = errorCodeInterface;
    this.errorDescription = errorDescription;
  }

  public ApiException(ErrorCodeInterface errorCodeInterface, Throwable tx) {
    super(tx);
    this.errorCodeInterface = errorCodeInterface;
    this.errorDescription = errorCodeInterface.getDescription();
  }

  public ApiException(ErrorCodeInterface errorCodeInterface, Throwable tx, String errorDescription) {
    super(tx);
    this.errorCodeInterface = errorCodeInterface;
    this.errorDescription = errorDescription;
  }
}