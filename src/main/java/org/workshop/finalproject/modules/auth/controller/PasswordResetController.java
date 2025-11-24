package org.workshop.finalproject.modules.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.workshop.finalproject.modules.auth.dto.ForgotPasswordRequestDTO;
import org.workshop.finalproject.modules.auth.dto.ResetPasswordRequestDTO;
import org.workshop.finalproject.modules.auth.service.PasswordResetService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO dto) {
        passwordResetService.generateResetCode(dto.getEmail());
        return ResponseEntity.ok(Map.of("message", "Reset code sent if email exists"));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO dto) {
        passwordResetService.resetPassword(dto.getCode(), dto.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    //Don't really need this, but leave just in case for now

    @GetMapping("/check_code")
    public ResponseEntity<?> checkCode(@RequestParam String code) {
        boolean valid = passwordResetService.isCodeValid(code);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}