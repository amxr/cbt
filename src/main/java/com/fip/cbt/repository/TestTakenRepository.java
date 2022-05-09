package com.fip.cbt.repository;

import com.fip.cbt.model.TestTaken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TestTakenRepository extends MongoRepository<TestTaken, String> {
    Optional<TestTaken> findByCandidate(String username);
    
    Optional<TestTaken> findByTestId(String testId);
    
    Optional<List<TestTaken>> findBySubmissionDate(LocalDateTime submissionDate);
    
    //Optional<List<TestTaken>> findByPassed(boolean isPassed);
}
