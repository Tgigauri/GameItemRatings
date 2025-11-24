package org.workshop.finalproject.modules.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.workshop.finalproject.modules.comment.dto.CommentRequestDTO;
import org.workshop.finalproject.modules.comment.dto.CommentResponseDTO;
import org.workshop.finalproject.modules.comment.service.CommentService;

@RestController
@RequestMapping("/comments/anon")
@RequiredArgsConstructor
public class AnonymousCommentController {

    private final CommentService commentService;


    @PostMapping
    public CommentResponseDTO addAnonymousComment(
            @RequestBody @Valid CommentRequestDTO dto) {
        return commentService.addComment(null, dto);
    }
}
