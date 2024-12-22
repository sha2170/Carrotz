package com.carrotzmarket.api.domain.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String password;

    @Email
    private String email;

    private String phone;

    private Long regionId;
}
