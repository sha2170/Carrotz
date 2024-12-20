package com.carrotzmarket.api.domain.user.controller.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String password;

    private String profileImageUrl;

    @Email
    private String email;

    private String phone;

    private Long regionId;
}
