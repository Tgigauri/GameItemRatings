package org.workshop.finalproject.modules.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellerRatingDTO {
    private Long sellerId;
    private String sellerName;
    private int rating;
}
