package org.workshop.finalproject.modules.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.modules.user.dto.UserRequestDTO;
import org.workshop.finalproject.modules.user.dto.UserResponseDTO;
import org.workshop.finalproject.exception.EmailAlreadyExistsException;
import org.workshop.finalproject.modules.user.model.Role;
import org.workshop.finalproject.modules.user.model.User;
import org.workshop.finalproject.modules.user.repository.UserRepository;

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