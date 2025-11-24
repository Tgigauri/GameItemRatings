package org.workshop.finalproject.modules.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.modules.item.dto.ItemRequestDTO;
import org.workshop.finalproject.modules.item.dto.ItemResponseDTO;
import org.workshop.finalproject.modules.item.dto.ItemUpdateDTO;
import org.workshop.finalproject.modules.game.model.Game;
import org.workshop.finalproject.modules.item.model.Item;
import org.workshop.finalproject.modules.user.model.Role;
import org.workshop.finalproject.modules.user.model.User;
import org.workshop.finalproject.modules.game.repository.GameRepository;
import org.workshop.finalproject.modules.item.repository.ItemRepository;
import org.workshop.finalproject.modules.user.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Transactional
    public ItemResponseDTO createItem(Principal principal, ItemRequestDTO dto) {
        User seller = getUserFromPrincipal(principal);
        checkApprovedSeller(seller);

        Game game = gameRepository.findGameByGameName(dto.getGameName())
                .orElseGet(() -> {
                    Game g = new Game();
                    g.setGameName(dto.getGameName());
                    return gameRepository.save(g);
                });

        Item item = new Item();
        item.setTitle(dto.getItemTitle());
        item.setText(dto.getItemDescription());
        item.setSeller(seller);
        item.setGame(game);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        item = itemRepository.save(item);
        return toResponse(item);
    }

    @Transactional
    public ItemResponseDTO updateItem(Principal principal, Long itemId, ItemUpdateDTO dto) {
        User currentUser = getUserFromPrincipal(principal);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!Objects.equals(item.getSeller(), currentUser)) {
            throw new RuntimeException("You are not allowed to edit this item");
        }

        checkApprovedSeller(currentUser);

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            item.setTitle(dto.getTitle());
        }
        if (dto.getText() != null && !dto.getText().isBlank()) {
            item.setText(dto.getText());
        }
        if (dto.getGameName() != null && !dto.getGameName().isBlank()) {
            Game game = gameRepository.findGameByGameName(dto.getGameName())
                    .orElseGet(() -> {
                        Game g = new Game();
                        g.setGameName(dto.getGameName());
                        return gameRepository.save(g);
                    });
            item.setGame(game);
        }

        item.setUpdatedAt(LocalDateTime.now());
        return toResponse(itemRepository.save(item));
    }

    public List<ItemResponseDTO> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ItemResponseDTO getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return toResponse(item);
    }

    @Transactional
    public void deleteItem(Principal principal, Long itemId) {
        User currentUser = getUserFromPrincipal(principal);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!Objects.equals(item.getSeller(), currentUser)) {
            throw new RuntimeException("You are not allowed to delete this item");
        }

        itemRepository.delete(item);
    }

    private ItemResponseDTO toResponse(Item item) {
        return ItemResponseDTO.builder()
                .id(item.getId())
                .itemTitle(item.getTitle())
                .itemDescription(item.getText())
                .gameId(item.getGame().getId())
                .gameName(item.getGame().getGameName())
                .sellerId(item.getSeller().getId())
                .sellerName(item.getSeller().getFirstName())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    public void checkApprovedSeller(User user) {
        if (user.getRole() != Role.SELLER) {
            throw new RuntimeException("Only sellers allowed");
        }
        if (!user.isApproved()) {
            throw new RuntimeException("Seller not approved by admin yet");
        }
    }

    private User getUserFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You must be logged in to perform this action");
        }
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}