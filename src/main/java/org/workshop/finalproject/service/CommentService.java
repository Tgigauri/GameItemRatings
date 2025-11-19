package org.workshop.finalproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.workshop.finalproject.dto.CommentRequestDTO;
import org.workshop.finalproject.dto.CommentResponseDTO;
import org.workshop.finalproject.dto.CommentUpdateDTO;
import org.workshop.finalproject.model.Comment;
import org.workshop.finalproject.model.Game;
import org.workshop.finalproject.model.Item;
import org.workshop.finalproject.model.User;
import org.workshop.finalproject.repository.CommentRepository;
import org.workshop.finalproject.repository.GameRepository;
import org.workshop.finalproject.repository.ItemRepository;
import org.workshop.finalproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        return mapToResponse(saved);
    }

    public List<CommentResponseDTO> getCommentsByUser(Long userId) {
        List<Comment> comments = commentRepository.findCommentByAuthorId(userId);

        return comments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CommentResponseDTO getComment(Long userId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not Found"));

        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new RuntimeException("You are not allowed to view this comment");
        }

        return mapToResponse(comment);
    }

    @Transactional
    public CommentResponseDTO updateComment(Long userId, Long commentId, CommentUpdateDTO dto) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not Found"));

        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new RuntimeException("You are not allowed to edit this comment");
        }

        if (dto.getMessage() != null && !dto.getMessage().isBlank()) {
            comment.setComment(dto.getMessage());
        }

        commentRepository.save(comment);

        return mapToResponse(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not Found"));

        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new RuntimeException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }

    private CommentResponseDTO mapToResponse(Comment comment) {
        Item item = comment.getItem();
        Game game = item.getGame();
        User author = comment.getAuthor();

        return CommentResponseDTO.builder()
                .id(comment.getId())
                .message(comment.getComment())
                .authorId(author.getId())
                .authorName(author.getFirstName())
                .itemId(item.getId())
                .itemTitle(item.getTitle())
                .text(item.getText())
                .gameId(game.getId())
                .gameName(game.getGameName())
                .createdAt(comment.getCreatedAt())
                .approved(comment.isApproved())
                .build();
    }
}
