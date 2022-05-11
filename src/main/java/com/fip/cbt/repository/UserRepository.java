package com.fip.cbt.repository;

import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{email:'?0'}")
    Optional<User> findUserByEmail(String email);

    @Query("{role: '?0'}")
    List<User> findUserByRole(Role role);
}
