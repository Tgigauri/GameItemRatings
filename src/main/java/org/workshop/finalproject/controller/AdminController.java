package org.workshop.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.workshop.finalproject.dto.CommentResponseDTO;
import org.workshop.finalproject.model.Comment;
import org.workshop.finalproject.model.User;
import org.workshop.finalproject.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/pending-sellers")
    public List<User> getPendingSellers() {
        return adminService.getPendingSellers();
    }

    @PutMapping("/sellers/{id}/approve")
    public User approveSeller(@PathVariable Long id) {
        return adminService.approveSeller(id);
    }

    @PutMapping("/sellers/{id}/reject")
    public User rejectSeller(@PathVariable Long id) {
        return adminService.rejectSeller(id);
    }

    @GetMapping("/pending-comments")
    public List<CommentResponseDTO> getPendingComments() {
        return adminService.getPendingComments();
    }

    @PutMapping("/comments/{id}/approve")
    public CommentResponseDTO approveComment(@PathVariable Long id) {
        return adminService.approveComment(id);
    }

    @PutMapping("/comments/{id}/reject")
    public CommentResponseDTO rejectComment(@PathVariable Long id) {
        return adminService.rejectComment(id);
    }
}
