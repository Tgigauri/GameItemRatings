package org.workshop.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.workshop.finalproject.dto.ItemRequestDTO;
import org.workshop.finalproject.dto.ItemResponseDTO;
import org.workshop.finalproject.dto.ItemUpdateDTO;
import org.workshop.finalproject.service.ItemService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDTO createItem(@RequestBody @Valid ItemRequestDTO dto, Principal principal) {
        return itemService.createItem(principal, dto);
    }

    @PutMapping("/{itemId}")
    public ItemResponseDTO updateItem(@PathVariable Long itemId,
                                      @RequestBody ItemUpdateDTO dto,
                                      Principal principal) {
        return itemService.updateItem(principal, itemId, dto);
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
    public void deleteItem(@PathVariable Long itemId, Principal principal) {
        itemService.deleteItem(principal, itemId);
    }
}