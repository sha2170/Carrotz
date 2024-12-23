package com.carrotzmarket.api.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {

    @NotNull
    private String loginId;

    @NotNull
    private String password;
}
