package com.carrotzmarket.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Api<T> {

    private Result result;
    private T data;
    private String message;
    private int status;

    // 응답 성공
    public static <T> Api<T> OK(T data) {
        Api<T> api = new Api<>();
        api.status = 200;
        api.result = Result.OK();
        api.data = data;
        return api;
    }

    // 에러 처리
    public static Api<Object> ERROR(Result result){
        Api<Object> api = new Api<>();
        api.result = result;
        return api;
    }

    public static <T> Api<T> unauthorized(String message) {
        Api<T> api = new Api<>();
        api.status = 401; // HTTP 401 Unauthorized
        api.message = message;
        api.data = null;
        return api;
    }
}
