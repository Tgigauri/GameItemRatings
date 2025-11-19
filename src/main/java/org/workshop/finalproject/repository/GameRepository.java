package org.workshop.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workshop.finalproject.model.Game;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findGameByGameName(String title);

    Optional<Game> findGameById(Long id);
}