package org.workshop.finalproject.modules.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.workshop.finalproject.modules.comment.dto.CommentRequestDTO;
import org.workshop.finalproject.modules.comment.dto.CommentResponseDTO;
import org.workshop.finalproject.modules.comment.dto.CommentUpdateDTO;
import org.workshop.finalproject.modules.comment.service.CommentService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponseDTO addComment(
            @RequestBody @Valid CommentRequestDTO dto,
            Principal principal) {
        return commentService.addComment(principal, dto);
    }

    @GetMapping("/my_comments")
    public List<CommentResponseDTO> getMyComments(Principal principal) {
        return commentService.getCommentsByUser(principal);
    }

    @GetMapping("/{commentId}")
    public CommentResponseDTO getComment(@PathVariable Long commentId, Principal principal) {
        return commentService.getComment(principal, commentId);
    }

    @PutMapping("/{commentId}")
    public CommentResponseDTO updateComment(@PathVariable Long commentId,
                                            @RequestBody CommentUpdateDTO dto,
                                            Principal principal) {
        return commentService.updateComment(principal, commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId, Principal principal) {
        commentService.deleteComment(principal, commentId);
    }
}