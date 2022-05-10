package com.fip.cbt.repository;

import com.fip.cbt.model.TestTaken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TestTakenRepository extends MongoRepository<TestTaken, String> {
    
    @Query("{username:'?0'}")
    Optional<TestTaken> findByCandidate(String username);
    
    @Query("{testId:'?0'}")
    Optional<TestTaken> findByTestId(String testId);
    
    @Query("{submissionDate:'?0'}")
    Optional<List<TestTaken>> findBySubmissionDate(LocalDateTime submissionDate);
    
    //Optional<List<TestTaken>> findByPassed(boolean isPassed);
}
