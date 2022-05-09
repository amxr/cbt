package com.fip.cbt.repository;

import com.fip.cbt.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TestRepository extends MongoRepository<Test, String> {

    @Query("{testNumber:'?0'}")
    Optional<Test> findTestByTestNumber(String testNumber);

   @Query("{name:'?0'}")
    Optional<Test> findTestByName(String name);

   @Query("{created:'?0'}")
    Optional<Test> findTestByDate(LocalDateTime created);

   @Query("{duration:'?0'}")
    Optional<Test> findTestByDuration(int duration);

    long count();
}
