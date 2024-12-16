package com.carrotzmarket.db.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(UserEntity user);
    Optional<UserEntity> findById(Long id); // Optional로 변경
    List<UserEntity> findAll();
    Optional<UserEntity> findByLoginId(String loginId); // Optional로 변경
    void deleteById(Long id);
}
