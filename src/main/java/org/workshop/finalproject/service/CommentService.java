package org.workshop.finalproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.dto.CommentRequestDTO;
import org.workshop.finalproject.dto.CommentResponseDTO;
import org.workshop.finalproject.model.Comment;
import org.workshop.finalproject.model.Game;
import org.workshop.finalproject.model.Item;
import org.workshop.finalproject.model.User;
import org.workshop.finalproject.repository.CommentRepository;
import org.workshop.finalproject.repository.GameRepository;
import org.workshop.finalproject.repository.ItemRepository;
import org.workshop.finalproject.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final GameRepository gameRepository;

    @Transactional
    public CommentResponseDTO addComment(Long userId, CommentRequestDTO dto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not Found"));

        Game game = gameRepository.findGameById(dto.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Game not Found"));

        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Game Item not Found"));

        Comment comment = Comment.builder()
                .comment(dto.getMessage())
                .author(author)
                .item(item)
                .createdAt(LocalDateTime.now())
                .approved(false)
                .build();

        Comment saved = commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .id(saved.getId())
                .message(saved.getComment())
                .authorId(author.getId())
                .authorName(author.getFirstName())
                .itemTitle(item.getTitle())
                .itemId(item.getId())
                .createdAt(saved.getCreatedAt())
                .text(item.getText())
                .gameId(game.getId())
                .gameName(game.getGameName())
                .build();
    }
}
