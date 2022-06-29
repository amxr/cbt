package com.fip.cbt.repository;

import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);

    Optional<User> findByEmailIgnoreCase(String email);
}
