package org.workshop.finalproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.workshop.finalproject.modules.comment.dto.CommentRequestDTO;
import org.workshop.finalproject.modules.comment.service.CommentService;
import org.workshop.finalproject.modules.game.model.Game;
import org.workshop.finalproject.modules.game.repository.GameRepository;
import org.workshop.finalproject.modules.item.dto.ItemRequestDTO;
import org.workshop.finalproject.modules.item.model.Item;
import org.workshop.finalproject.modules.item.repository.ItemRepository;
import org.workshop.finalproject.modules.item.service.ItemService;
import org.workshop.finalproject.modules.user.model.Role;
import org.workshop.finalproject.modules.user.model.User;
import org.workshop.finalproject.modules.user.repository.UserRepository;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private GameRepository gameRepository;

    @Test
    void addComment_withApprovedUser_shouldSaveComment() {
        User seller = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .password("password123")
                .role(Role.SELLER)
                .approved(true)
                .build();
        userRepository.save(seller);

        Game game = Game.builder()
                .gameName("TestGame")
                .build();
        gameRepository.save(game);

        Item item = Item.builder()
                .title("TestItem")
                .text("TestDescription")
                .seller(seller)
                .game(game)
                .build();
        itemRepository.save(item);

        CommentRequestDTO commentDto = new CommentRequestDTO();
        commentDto.setItemId(item.getId());
        commentDto.setMessage("Hello There");

        Principal principal = seller::getEmail;

        var response = commentService.addComment(principal, commentDto);

        assertNotNull(response.getId());
        assertEquals("Hello There", response.getMessage());
    }

    @Test
    void createItem_withUnapprovedSeller_shouldThrow() {
        User seller = User.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john@example.com")
                .password("password123")
                .role(Role.SELLER)
                .approved(false)
                .build();
        userRepository.save(seller);

        Game game = Game.builder()
                .gameName("TestGame2")
                .build();
        gameRepository.save(game);

        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setItemTitle("TestItem2");
        dto.setItemDescription("TestDescription2");
        dto.setGameName(game.getGameName());

        Principal principal = seller::getEmail;

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> itemService.createItem(principal, dto));

        assertEquals("Seller not approved by admin yet", ex.getMessage());
    }
}
