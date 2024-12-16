package com.carrotzmarket.db.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키 아이디

    @Column(length = 100, name = "login_id", nullable = false, unique = true)
    private String loginid; // 로그인 아이디, 중복 방지

    @Column(length = 100, nullable = false)
    private String password; // 비밀번호

    @Column(length = 100, nullable = false, unique = true)
    private String email; // 이메일, 중복 방지

    @Column(length = 100)
    private String phone; // 연락처

    private LocalDate birthday; // 생일

    @Column(name = "profile_image_url", length = 255)
    private String profile_iamge_url;

    @Column(name = "is_deleted")
    private boolean isDeleted; // 삭제 여부 / 1, 0 으로 구분하여 확인

    private LocalDateTime createdAt; // 생성 날짜
    private LocalDateTime lastLoginAt; // 최근 로그인 날짜


}
