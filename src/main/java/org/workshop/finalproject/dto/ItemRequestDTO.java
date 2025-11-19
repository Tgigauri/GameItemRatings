package org.workshop.finalproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemRequestDTO {

    @NotBlank
    private String itemTitle;

    @NotBlank
    private String itemDescription;

    @NotNull
    private String gameName;
}
