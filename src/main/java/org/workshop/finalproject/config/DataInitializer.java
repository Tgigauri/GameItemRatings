package org.workshop.finalproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.workshop.finalproject.model.Game;
import org.workshop.finalproject.model.Item;
import org.workshop.finalproject.model.Role;
import org.workshop.finalproject.model.User;
import org.workshop.finalproject.repository.GameRepository;
import org.workshop.finalproject.repository.ItemRepository;
import org.workshop.finalproject.repository.UserRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ItemRepository itemRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("test@test.com")
                    .password("very nice password")
                    .role(Role.SELLER)
                    .approved(true)
                    .build();
            userRepository.save(admin);

            Game game = new Game();
            game.setGameName("CS:GO");
            gameRepository.save(game);

            Item item = new Item();
            item.setTitle("Karambit");
            item.setText("Fade");
            item.setSeller(admin);
            item.setGame(game);
            item.setCreatedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());
            itemRepository.save(item);
        }
    }
}
