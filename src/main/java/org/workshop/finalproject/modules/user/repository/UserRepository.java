package org.workshop.finalproject.modules.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workshop.finalproject.modules.user.model.Role;
import org.workshop.finalproject.modules.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByRoleAndApprovedFalse(Role role);
    List<User> findAllByRoleAndApprovedTrue(Role role);
    boolean existsByEmail(String email);
}
