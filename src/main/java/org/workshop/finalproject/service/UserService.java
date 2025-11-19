package org.workshop.finalproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.dto.UserRequestDTO;
import org.workshop.finalproject.dto.UserResponseDTO;
import org.workshop.finalproject.exception.EmailAlreadyExistsException;
import org.workshop.finalproject.model.Role;
import org.workshop.finalproject.model.User;
import org.workshop.finalproject.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.SELLER)
                .approved(false)
                .build();

        User saved = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .role(saved.getRole().name())
                .approved(saved.isApproved())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}