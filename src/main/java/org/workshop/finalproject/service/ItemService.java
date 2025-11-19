package org.workshop.finalproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.dto.ItemRequestDTO;
import org.workshop.finalproject.dto.ItemResponseDTO;
import org.workshop.finalproject.dto.ItemUpdateDTO;
import org.workshop.finalproject.model.Game;
import org.workshop.finalproject.model.Item;
import org.workshop.finalproject.model.User;
import org.workshop.finalproject.repository.GameRepository;
import org.workshop.finalproject.repository.ItemRepository;
import org.workshop.finalproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Transactional
    public ItemResponseDTO createItem(Long userId, ItemRequestDTO dto) {


        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


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
    public ItemResponseDTO updateItem(Long itemId, Long userId, ItemUpdateDTO dto) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!Objects.equals(item.getSeller().getId(), userId)) {
            throw new RuntimeException("You are not allowed to edit this item");
        }

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

        itemRepository.save(item);

        return toResponse(item);
    }

    public List<ItemResponseDTO> getAllItems() {

        List<Item> items = itemRepository.findAll();

        return items.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<ItemResponseDTO> getAllItems(Pageable pageable) {

        return itemRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public List<ItemResponseDTO> getItemsByUser(Long userId) {

        Optional<Item> items = itemRepository.findBySellerId(userId);

        return items.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteItem(Long itemId, Long userId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!Objects.equals(item.getSeller().getId(), userId)) {
            throw new RuntimeException("You are not allowed to delete this item");
        }

        itemRepository.delete(item);
    }

    public ItemResponseDTO getItemById(Long itemId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return toResponse(item);
    }

    private ItemResponseDTO toResponse(Item item) {
        ItemResponseDTO dto = new ItemResponseDTO();

        dto.setId(item.getId());
        dto.setItemTitle(item.getTitle());
        dto.setItemDescription(item.getText());
        dto.setGameId(item.getGame().getId());
        dto.setGameName(item.getGame().getGameName());
        dto.setSellerName(item.getSeller().getFirstName());
        dto.setSellerId(item.getSeller().getId());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());

        return dto;
    }

}