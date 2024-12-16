package com.carrotzmarket.db.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // SELECT * FROM user WHERE id = ?
    Optional<UserEntity> findById(Long id);

    // SELECT * FROM user WHERE loginid = ? AND password = ? AND is_deleted = false;
    Optional<UserEntity> findByLoginIdAndPasswordAndIsDeletedFalse(String loginId, String password);


}
