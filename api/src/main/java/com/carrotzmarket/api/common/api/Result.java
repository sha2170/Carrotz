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

    private boolean success; // 성공 or 실패 여부
    private String code;      // 응답 코드 ("OK", "ERROR" 등)
    private String message; // 결과

    // 성공
    public static Result OK() {
        return new Result(true, "OK", "Success");
    }

    // 실패
    public static Result ERROR(String message) {
        return new Result(false, "ERROR", message);
    }
}
