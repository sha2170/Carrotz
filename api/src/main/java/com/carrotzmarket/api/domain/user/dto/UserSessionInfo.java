package com.carrotzmarket.api.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionInfo {

    private Long id;
    private String loginId;
    private String email;
    private String phone;
    private String profileImageUrl;
    private String regionName;
}
