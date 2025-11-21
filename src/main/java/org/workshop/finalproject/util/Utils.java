package org.workshop.finalproject.util;

import org.workshop.finalproject.dto.CommentResponseDTO;
import org.workshop.finalproject.model.Comment;
import org.workshop.finalproject.model.Game;
import org.workshop.finalproject.model.Item;
import org.workshop.finalproject.model.User;

public class Utils {
    public static CommentResponseDTO mapToResponse(Comment comment) {
        Item item = comment.getItem();
        Game game = item.getGame();
        User author = comment.getAuthor();

        CommentResponseDTO.CommentResponseDTOBuilder builder = CommentResponseDTO.builder()
                .id(comment.getId())
                .message(comment.getComment())
                .itemId(item.getId())
                .itemTitle(item.getTitle())
                .text(item.getText())
                .gameId(game.getId())
                .gameName(game.getGameName())
                .createdAt(comment.getCreatedAt())
                .approved(comment.isApproved());

        if (author != null) {
            builder.authorId(author.getId())
                    .authorName(author.getFirstName());
        } else {
            builder.authorId(null)
                    .authorName("Anonymous");
        }

        return builder.build();
    }
}
