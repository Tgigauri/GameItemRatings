package org.workshop.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.workshop.finalproject.dto.ItemRequestDTO;
import org.workshop.finalproject.dto.ItemResponseDTO;
import org.workshop.finalproject.dto.ItemUpdateDTO;
import org.workshop.finalproject.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/object")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @PostMapping
    public ItemResponseDTO createItem(
            @RequestParam Long userId, // author/seller ID
            @RequestBody @Valid ItemRequestDTO dto) {

        return itemService.createItem(userId, dto);
    }


    @PutMapping("/{itemId}")
    public ItemResponseDTO updateItem(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestBody ItemUpdateDTO dto) {

        return itemService.updateItem(itemId, userId, dto);
    }


    @GetMapping
    public List<ItemResponseDTO> getAllItems() {
        return itemService.getAllItems();
    }


    @GetMapping("/{itemId}")
    public ItemResponseDTO getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }


    @DeleteMapping("/{itemId}")
    public void deleteItem(
            @PathVariable Long itemId,
            @RequestParam Long userId) {

        itemService.deleteItem(itemId, userId);
    }
}
