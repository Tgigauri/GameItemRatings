package org.workshop.finalproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.workshop.finalproject.model.Role;
import org.workshop.finalproject.model.User;
import org.workshop.finalproject.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<User> getPendingSellers() {
        return userRepository.findAllByRoleAndApprovedFalse(Role.SELLER);
    }

    @Transactional
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
    public User rejectSeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        if (seller.getRole() != Role.SELLER) {
            throw new RuntimeException("User is not a seller");
        }

        seller.setApproved(false);
        return userRepository.save(seller);
    }
}
