package org.workshop.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workshop.finalproject.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentByAuthorId(Long id);

}

