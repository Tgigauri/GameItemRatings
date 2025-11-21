package org.workshop.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.workshop.finalproject.dto.SellerRatingDTO;
import org.workshop.finalproject.service.AnalyticsService;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/sellers/ratings")
    public List<SellerRatingDTO> getAllSellerRatings(@RequestParam(required = false) Integer limit) {
        return analyticsService.getAllSellerRatings(limit);
    }

    @GetMapping("/sellers/filter")
    public List<SellerRatingDTO> getTopSellersByGame(
            @RequestParam String gameName,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating) {

        return analyticsService.getTopSellersByGame(gameName, limit, minRating, maxRating);
    }
}