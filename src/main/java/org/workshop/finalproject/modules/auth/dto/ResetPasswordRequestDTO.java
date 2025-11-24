package org.workshop.finalproject.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    @NotBlank
    private String code;

    @NotBlank
    private String newPassword;
}
