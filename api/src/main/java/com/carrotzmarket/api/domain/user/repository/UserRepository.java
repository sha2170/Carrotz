package com.carrotzmarket.api.domain.user.repository;

import com.carrotzmarket.db.user.UserEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository{

    private final EntityManager em;

    public void save(UserEntity user) {
        em.persist(user);
    }

    public Optional<UserEntity> findById(Long id) {
        UserEntity user = em.find(UserEntity.class, id);
        return Optional.ofNullable(user); // Optional로 감싸 반환
    }

    public List<UserEntity> findAll() {
        return em.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }


    public Optional<UserEntity> findByLoginId(String loginId) {
        try {
            UserEntity user = em.createQuery("SELECT u FROM UserEntity u WHERE u.loginid = :loginId", UserEntity.class)
                    .setParameter("loginId", loginId)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty(); // 데이터가 없으면 Optional.empty() 반환
        }
    }


    public void deleteById(Long id) {
        UserEntity user = em.find(UserEntity.class, id);
        if (user != null) {
            em.remove(user);
        }
    }
}
