package com.carrotzmarket.api.common.error;

public interface ErrorCodeInterface {
    Integer getHttpStatusCode();
    Integer getErrorCode();
    String getDescription();

}
