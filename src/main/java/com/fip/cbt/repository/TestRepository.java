package com.fip.cbt.repository;

import com.fip.cbt.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends MongoRepository<Test, String> {
    long count();
}
