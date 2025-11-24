package org.workshop.finalproject.modules.auth.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.modules.user.model.User;
import org.workshop.finalproject.modules.user.repository.UserRepository;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final Duration CODE_TTL = Duration.ofMinutes(15);

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public void generateResetCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String code = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(code, email, CODE_TTL);

        // TODO: integrate email service; log for now
        log.info("Password reset code for {}: {}", email, code);
    }

    public boolean isCodeValid(String code) {
        return redisTemplate.hasKey(code);
    }

    public void resetPassword(String code, String newPassword) {
        String email = redisTemplate.opsForValue().get(code);
        if (email == null) {
            throw new RuntimeException("Invalid or expired code");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redisTemplate.delete(code);

        log.info("Password reset successfully for user: {}", email);
    }
}