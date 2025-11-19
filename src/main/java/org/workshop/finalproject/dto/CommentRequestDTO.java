package org.workshop.finalproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CommentRequestDTO {
    @NotBlank(message = "Comment cannot be empty")
    private String message;

    @NotNull(message = "Item id is required")
    private Long itemId;

    @NotNull(message = "Game id is required")
    private Long gameId;

}

