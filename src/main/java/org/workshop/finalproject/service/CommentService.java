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
import org.workshop.finalproject.util.Utils;

import java.security.Principal;
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
    public CommentResponseDTO addComment(Principal principal, CommentRequestDTO dto) {
        User author = null;

        if (principal != null) {
            author = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!author.isApproved()) {
                throw new RuntimeException("Your account is not approved yet");
            }
        }

        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Comment comment = Comment.builder()
                .comment(dto.getMessage())
                .author(author)
                .item(item)
                .createdAt(LocalDateTime.now())
                .approved(author != null)
                .build();

        Comment saved = commentRepository.save(comment);
        return Utils.mapToResponse(saved);
    }

    public List<CommentResponseDTO> getCommentsByUser(Principal principal) {
        User currentUser = getUserFromPrincipal(principal);

        List<Comment> comments = commentRepository.findCommentByAuthorId(currentUser.getId());
        return comments.stream()
                .map(Utils::mapToResponse)
                .collect(Collectors.toList());
    }

    public CommentResponseDTO getComment(Principal principal, Long commentId) {
        User currentUser = getUserFromPrincipal(principal);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!Objects.equals(comment.getAuthor(), currentUser)) {
            throw new RuntimeException("You cannot view this comment");
        }

        return Utils.mapToResponse(comment);
    }

    @Transactional
    public CommentResponseDTO updateComment(Principal principal, Long commentId, CommentUpdateDTO dto) {
        User currentUser = getUserFromPrincipal(principal);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!Objects.equals(comment.getAuthor(), currentUser)) {
            throw new RuntimeException("You cannot edit this comment");
        }

        if (dto.getMessage() != null && !dto.getMessage().isBlank()) {
            comment.setComment(dto.getMessage());
        }

        return Utils.mapToResponse(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Principal principal, Long commentId) {
        User currentUser = getUserFromPrincipal(principal);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!Objects.equals(comment.getAuthor(), currentUser)) {
            throw new RuntimeException("You cannot delete this comment");
        }

        commentRepository.delete(comment);
    }

    private User getUserFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Anonymous users cannot perform this action");
        }
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}