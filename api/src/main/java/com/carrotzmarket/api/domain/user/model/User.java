package com.carrotzmarket.api.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.plaf.synth.Region;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;

    private String loginId;

    private String password;

    private String email;

    private String phone;

    private LocalDate birthday;

    private String profileImageUrl;

    private Region region;

    private boolean isDeleted;

    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

}
