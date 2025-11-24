package org.workshop.finalproject.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckCodeRequestDTO {
    @NotBlank
    private String code;
}
