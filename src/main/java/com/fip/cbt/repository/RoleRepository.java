package com.fip.cbt.repository;

import com.fip.cbt.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, Long> {

    @Query("{name:'?0'}")
    Optional<Role> findByName(String name);
}
