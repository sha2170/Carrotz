package com.carrotzmarket.db.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // SELECT * FROM user WHERE id = ? ORDER BY id DESC LIMIT 1
    Optional<UserEntity> findFirstByIdOrderByDesc(Long id);



    Optional<UserEntity> findByLoginId(String loginId);



}
