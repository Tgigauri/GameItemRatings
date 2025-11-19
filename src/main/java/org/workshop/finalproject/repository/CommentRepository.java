package org.workshop.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workshop.finalproject.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {}

