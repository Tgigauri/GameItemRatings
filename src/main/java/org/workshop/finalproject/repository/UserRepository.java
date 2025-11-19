package org.workshop.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workshop.finalproject.model.Role;
import org.workshop.finalproject.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByRoleAndApprovedFalse(Role role);
}
