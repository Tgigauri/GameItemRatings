package org.workshop.finalproject.modules.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workshop.finalproject.modules.game.model.Game;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findGameByGameName(String title);

    Optional<Game> findGameById(Long id);
}