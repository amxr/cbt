package com.fip.cbt.repository;

import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    List<User> findUserByRole(Role role);
}
