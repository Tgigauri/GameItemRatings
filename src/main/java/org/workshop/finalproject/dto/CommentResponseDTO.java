package org.workshop.finalproject.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponseDTO {

    private Long id;
    private String message;
    private Long authorId;
    private String authorName;
    private boolean approved;
    private LocalDateTime createdAt;

    private Long itemId;
    private String itemTitle;
    private Long gameId;
    private String gameName;
    private String text;
}
