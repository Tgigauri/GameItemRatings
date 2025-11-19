package org.workshop.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workshop.finalproject.model.Game;
import org.workshop.finalproject.model.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByTitleAndGame(String title, Game game);
    Optional<Item> findById(Long itemId);
    Optional<Item> findBySellerId(Long sellerId);
}
