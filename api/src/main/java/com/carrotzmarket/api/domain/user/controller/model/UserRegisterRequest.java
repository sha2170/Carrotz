package com.carrotzmarket.api.domain.user.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;

    private String phone;

    private LocalDate birthday;

    private Long regionId;
}
