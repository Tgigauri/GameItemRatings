package org.workshop.finalproject.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.workshop.finalproject.model.*;
import org.workshop.finalproject.repository.CommentRepository;
import org.workshop.finalproject.repository.GameRepository;
import org.workshop.finalproject.repository.ItemRepository;
import org.workshop.finalproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        List<String> firstNames = List.of("Alice", "Bob", "Charlie", "Diana", "Ethan");
        List<String> lastNames = List.of("Smith", "Johnson", "Williams", "Brown", "Jones");
        List<String> gameNames = List.of("CS:GO", "Dota 2", "FIFA 23", "Team Fortress 2", "Valorant");
        List<String> itemTitles = List.of("Karambit", "AWP", "FIFA Ultimate Player", "Rocket Launcher", "Phantom");
        List<String> itemDescriptions = List.of("Rare skin", "Legendary item", "Top-tier stats", "Limited edition", "Epic quality");

        List<Game> games = gameNames.stream()
                .map(name -> gameRepository.save(Game.builder().gameName(name).build()))
                .toList();

        List<User> sellers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User seller = User.builder()
                    .firstName(firstNames.get(i))
                    .lastName(lastNames.get(i))
                    .email("seller" + i + "@example.com")
                    .password("password" + i)
                    .role(Role.SELLER)
                    .approved(true)
                    .build();
            sellers.add(userRepository.save(seller));
        }

        List<Item> items = new ArrayList<>();
        for (User seller : sellers) {
            for (int i = 0; i < 3; i++) {
                Game game = games.get(random.nextInt(games.size()));
                Item item = Item.builder()
                        .title(itemTitles.get(random.nextInt(itemTitles.size())))
                        .text(itemDescriptions.get(random.nextInt(itemDescriptions.size())))
                        .seller(seller)
                        .game(game)
                        .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                        .updatedAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                        .build();
                items.add(itemRepository.save(item));
            }
        }

        User randomAuthor = sellers.get(random.nextInt(sellers.size()));
        for (Item item : items) {
            int commentsCount = 1 + random.nextInt(5);
            for (int j = 0; j < commentsCount; j++) {
                Comment comment = Comment.builder()
                        .comment("This is a comment #" + j + " for " + item.getTitle())
                        .item(item)
                        .author(randomAuthor)
                        .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                        .approved(true)
                        .build();
                commentRepository.save(comment);
            }
        }


        System.out.println("DataInitializer: Created " + sellers.size() + " sellers, "
                + games.size() + " games, " + items.size() + " items with comments.");
    }
}
