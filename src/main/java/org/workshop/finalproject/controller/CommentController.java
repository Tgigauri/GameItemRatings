package org.workshop.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.workshop.finalproject.dto.CommentRequestDTO;
import org.workshop.finalproject.dto.CommentResponseDTO;
import org.workshop.finalproject.service.CommentService;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponseDTO addComment(
            @PathVariable Integer userId,
            @RequestBody @Valid CommentRequestDTO dto) {
        return commentService.addComment(Long.valueOf(userId), dto);
    }
}
