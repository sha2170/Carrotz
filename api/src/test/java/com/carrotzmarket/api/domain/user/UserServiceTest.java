package com.carrotzmarket.api.domain.user;

import com.carrotzmarket.api.domain.user.dto.UserRegisterRequestDto;
import com.carrotzmarket.api.domain.user.dto.UserResponseDto;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.api.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUserSuccessfully() {
        UserRegisterRequestDto request = new UserRegisterRequestDto(
                "testUser",
                "password123",
                "test@example.com",
                "010-1234-5678",
                LocalDate.of(1990, 1, 1),
                1L,
                null
        );

        UserResponseDto response = userService.register(request, null);

        assertNotNull(response);
        assertEquals("testUser", response.getLoginId());
    }
}

