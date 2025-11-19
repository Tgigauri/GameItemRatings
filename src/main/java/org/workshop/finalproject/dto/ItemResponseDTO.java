package org.workshop.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDTO {

    private Long id;
    private String itemTitle;
    private String itemDescription;
    private Long sellerId;
    private String sellerName;
    private Long gameId;
    private String gameName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}