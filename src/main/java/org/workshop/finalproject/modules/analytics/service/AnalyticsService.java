package org.workshop.finalproject.modules.analytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.modules.analytics.dto.SellerRatingDTO;
import org.workshop.finalproject.modules.game.model.Game;
import org.workshop.finalproject.modules.user.model.Role;
import org.workshop.finalproject.modules.comment.repository.CommentRepository;
import org.workshop.finalproject.modules.game.repository.GameRepository;
import org.workshop.finalproject.modules.user.repository.UserRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final GameRepository gameRepository;

    public List<SellerRatingDTO> getAllSellerRatings(Integer limit) {
        List<SellerRatingDTO> allSellers = userRepository.findAllByRoleAndApprovedTrue(Role.SELLER).stream()
                .map(seller -> SellerRatingDTO.builder()
                        .sellerId(seller.getId())
                        .sellerName(seller.getFirstName() + " " + seller.getLastName())
                        .rating(commentRepository.countByItem_SellerAndApprovedTrue(seller))
                        .build())
                .sorted(Comparator.comparingInt(SellerRatingDTO::getRating).reversed())
                .toList();

        if (limit != null && limit > 0) {
            return allSellers.stream()
                    .limit(limit)
                    .toList();
        }

        return allSellers;
    }

    public List<SellerRatingDTO> getTopSellersByGame(String gameName, int limit, Integer minRating, Integer maxRating) {
        Game game = gameRepository.findGameByGameName(gameName)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        return userRepository.findAllByRoleAndApprovedTrue(Role.SELLER).stream()
                .map(seller -> {
                    int rating = commentRepository.findAllByItemSellerAndApprovedTrueAndItemGame(seller, game).size();
                    return SellerRatingDTO.builder()
                            .sellerId(seller.getId())
                            .sellerName(seller.getFirstName() + " " + seller.getLastName())
                            .rating(rating)
                            .build();
                })
                .filter(dto -> {
                    boolean passesMin = minRating == null || dto.getRating() >= minRating;
                    boolean passesMax = maxRating == null || dto.getRating() <= maxRating;
                    return passesMin && passesMax;
                })
                .sorted(Comparator.comparingInt(SellerRatingDTO::getRating).reversed())
                .limit(limit)
                .toList();
    }
}

