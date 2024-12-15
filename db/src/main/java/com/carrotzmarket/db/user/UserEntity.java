package com.carrotzmarket.db.user;

import com.carrotzmarket.db.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String loginid;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100)
    private String phone;

    private LocalDate birthday;

    @Column(length = 255)
    private String profile_iamge_url;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;


}
