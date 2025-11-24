package org.workshop.finalproject.modules.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.workshop.finalproject.modules.auth.dto.AuthRequestDTO;
import org.workshop.finalproject.modules.auth.dto.AuthResponseDTO;
import org.workshop.finalproject.modules.auth.dto.RegisterRequestDTO;
import org.workshop.finalproject.modules.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequestDTO dto) {
        authService.register(dto);
    }

    @GetMapping("/confirm_email")
    public ResponseEntity<String> confirmEmail(@RequestParam String code) {
        authService.confirmEmail(code);
        return ResponseEntity.ok("Email confirmed! You can now login.");
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid AuthRequestDTO dto) {
        return authService.login(dto);
    }
}
