package com.carrotzmarket.api.common.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Api<T> {

    //
    private Result result;

    @Valid // 유효성 검사!
    private T data;


}
