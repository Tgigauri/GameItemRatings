package org.workshop.finalproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.workshop.finalproject.dto.CommentResponseDTO;
import org.workshop.finalproject.model.Comment;
import org.workshop.finalproject.model.Role;
import org.workshop.finalproject.model.User;
import org.workshop.finalproject.repository.CommentRepository;
import org.workshop.finalproject.repository.UserRepository;
import org.workshop.finalproject.util.Utils;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    //    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<User> getPendingSellers() {
        return userRepository.findAllByRoleAndApprovedFalse(Role.SELLER);
    }

    @Transactional
//    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User approveSeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        if (seller.getRole() != Role.SELLER) {
            throw new RuntimeException("User is not a seller");
        }

        seller.setApproved(true);
        return userRepository.save(seller);
    }

    @Transactional
//    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User rejectSeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        if (seller.getRole() != Role.SELLER) {
            throw new RuntimeException("User is not a seller");
        }

        seller.setApproved(false);
        return userRepository.save(seller);
    }

    public List<CommentResponseDTO> getPendingComments() {

        List<Comment> comments = commentRepository.findAllByApprovedFalse();

        return comments.stream()
                .map(Utils::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDTO approveComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.setApproved(true);
        commentRepository.save(comment);
        return Utils.mapToResponse(comment);
    }

    @Transactional
    public CommentResponseDTO rejectComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.setApproved(false);
         commentRepository.save(comment);
        return Utils.mapToResponse(comment);
    }
}
