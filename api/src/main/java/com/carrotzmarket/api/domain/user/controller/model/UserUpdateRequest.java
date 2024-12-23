package com.carrotzmarket.api.domain.user.controller.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String password;

    @Email
    private String email;

    private String phone;

    private Long regionId;

//    private MultipartFile profileImage;
}
