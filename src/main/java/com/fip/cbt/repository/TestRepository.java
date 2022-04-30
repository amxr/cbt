package com.fip.cbt.repository;

import com.fip.cbt.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends MongoRepository<Test, String> {

    @Query("{testNumber:'?0'}")
    Optional<Test> findTestByTestNumber(String testNumber);

    long count();
}
