package com.carrotzmarket.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    private boolean success;
    private String code;
    private String message;

    public static Result OK() {
        return new Result(true, "OK", "Success");
    }

    public static Result ERROR(String message) {
        return new Result(false, "ERROR", message);
    }
}
