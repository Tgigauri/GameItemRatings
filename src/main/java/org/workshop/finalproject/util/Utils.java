package org.workshop.finalproject.util;

import org.workshop.finalproject.modules.comment.dto.CommentResponseDTO;
import org.workshop.finalproject.modules.comment.model.Comment;
import org.workshop.finalproject.modules.game.model.Game;
import org.workshop.finalproject.modules.item.model.Item;
import org.workshop.finalproject.modules.user.model.User;

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
                    .authorName(author.getFirstName())
                    .anonymousId(null);
        } else {
            String anonId = comment.getAnonymousId();
            String anonShort = anonId != null ? anonId.substring(0, 6) : "000000";

            builder.authorId(null)
                    .authorName("Anonymous_" + anonShort)
                    .anonymousId(anonId);
        }

        return builder.build();
    }
}
