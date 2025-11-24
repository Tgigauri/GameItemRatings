package org.workshop.finalproject.modules.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.config.security.CustomUserDetails;
import org.workshop.finalproject.config.security.JwtUtil;
import org.workshop.finalproject.modules.auth.dto.AuthRequestDTO;
import org.workshop.finalproject.modules.auth.dto.AuthResponseDTO;
import org.workshop.finalproject.modules.auth.dto.RegisterRequestDTO;
import org.workshop.finalproject.modules.user.model.Role;
import org.workshop.finalproject.modules.user.model.User;
import org.workshop.finalproject.modules.user.repository.UserRepository;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final Duration CONFIRMATION_COUNTDOWN = Duration.ofHours(24);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;

    public void register(RegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.SELLER)
                .approved(false) // unapproved initially
                .build();

        userRepository.save(user);

        String code = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(code, user.getEmail(), CONFIRMATION_COUNTDOWN);

        String confirmationLink = "http://localhost:8080/auth/confirm_email?code=" + code;
        log.info("Confirmation link (simulate sending email): {}", confirmationLink);
    }

    public AuthResponseDTO login(AuthRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return generateTokenForUser(user);
    }

    private AuthResponseDTO generateTokenForUser(User user) {
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getEmail())
                .password(user.getPassword())
                .approved(user.isApproved())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .build();

        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponseDTO(token);
    }

    @Transactional
    public void confirmEmail(String code) {

        //For Testing Purposes the code will be logged in the console
        String email = redisTemplate.opsForValue().get(code);
        if (email == null) {
            throw new RuntimeException("Invalid or expired confirmation code");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setApproved(true);
        userRepository.save(user);

        redisTemplate.delete(code);
    }
}