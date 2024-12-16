package com.carrotzmarket.api.domain.user.service;

import com.carrotzmarket.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserEntity register(UserEntity userEntity) {
        userEntity.setRegisteredAt(LocalDateTime.now());
        return userRepository.save(userEntity);
    }

    public UserEntity login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserEntity getUserWithThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }
}
