package com.fip.cbt.repository;

import com.fip.cbt.model.ExamTaken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExamTakenRepository extends MongoRepository<ExamTaken, String> {
    
    @Query("{username:'?0'}")
    Optional<ExamTaken> findByCandidate(String username);
    
    @Query("{examId:'?0'}")
    Optional<ExamTaken> findByExamId(String examId);
    
    @Query("{submissionDate:'?0'}")
    Optional<List<ExamTaken>> findBySubmissionDate(LocalDateTime submissionDate);
    
    //Optional<List<ExamTaken>> findByPassed(boolean isPassed);
}
