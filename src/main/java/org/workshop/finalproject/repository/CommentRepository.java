package org.workshop.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workshop.finalproject.model.Comment;
import org.workshop.finalproject.model.Game;
import org.workshop.finalproject.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentByAuthorId(Long id);

    List<Comment> findAllByItemSellerAndApprovedTrue(User seller);

    List<Comment> findAllByItemSellerAndApprovedTrueAndItemGame(User seller, Game game);

    int countByItem_SellerAndApprovedTrue(User seller);

    List<Comment> findAllByApprovedFalse();


}

