package org.workshop.finalproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.config.security.CustomUserDetails;
import org.workshop.finalproject.config.security.JwtUtil;
import org.workshop.finalproject.dto.AuthRequestDTO;
import org.workshop.finalproject.dto.AuthResponseDTO;
import org.workshop.finalproject.dto.RegisterRequestDTO;
import org.workshop.finalproject.model.Role;
import org.workshop.finalproject.model.User;
import org.workshop.finalproject.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.SELLER)
                .approved(false)
                .build();

        userRepository.save(user);

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
}