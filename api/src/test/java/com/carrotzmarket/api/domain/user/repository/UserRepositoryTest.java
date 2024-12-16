package com.carrotzmarket.api.domain.user.repository;

import com.carrotzmarket.db.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSave() {
        // Given
        UserEntity user = new UserEntity();
        user.setLoginid("testuser");
        user.setPassword("password");

        // When
        userRepository.save(user);

        // Then
        Optional<UserEntity> savedUser = userRepository.findByLoginId("testuser");
        assertTrue(savedUser.isPresent());
        assertEquals("testuser", savedUser.get().getLoginid());
    }

    @Test
    public void testFindById() {
        // Given
        UserEntity user = new UserEntity();
        user.setLoginid("testuser");
        userRepository.save(user);

        // When
        Optional<UserEntity> foundUser = userRepository.findById(user.getId());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
    }

    @Test
    public void testFindByLoginId_UserNotFound() {
        // When
        Optional<UserEntity> result = userRepository.findByLoginId("nonexistent");

        // Then
        assertFalse(result.isPresent());
    }
}
