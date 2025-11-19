package org.workshop.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.workshop.finalproject.dto.CommentRequestDTO;
import org.workshop.finalproject.dto.CommentResponseDTO;
import org.workshop.finalproject.dto.CommentUpdateDTO;
import org.workshop.finalproject.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponseDTO addComment(
            @PathVariable(required = false) Long userId,
            @RequestBody @Valid CommentRequestDTO dto) {
        return commentService.addComment(userId, dto);
    }

    @GetMapping
    public List<CommentResponseDTO> getAllComments(
            @PathVariable Long userId) {

        return commentService.getCommentsByUser(userId);
    }

    @GetMapping("/{commentId}")
    public CommentResponseDTO getComment(
            @PathVariable Long userId,
            @PathVariable Long commentId) {

        return commentService.getComment(userId, commentId);
    }

    @PutMapping("/{commentId}")
    public CommentResponseDTO updateComment(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateDTO dto) {

        return commentService.updateComment(userId, commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long userId,
            @PathVariable Long commentId) {

        commentService.deleteComment(userId, commentId);
    }
}
