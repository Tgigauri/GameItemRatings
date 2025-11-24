package org.workshop.finalproject.modules.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workshop.finalproject.modules.game.model.Game;
import org.workshop.finalproject.modules.item.model.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByTitleAndGame(String title, Game game);

    Optional<Item> findById(Long itemId);

    Optional<Item> findBySellerId(Long sellerId);
}
