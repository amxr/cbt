package com.fip.cbt.repository;

import com.fip.cbt.model.Exam;
import com.fip.cbt.model.ExamTaken;
import com.fip.cbt.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExamTakenRepository extends MongoRepository<ExamTaken, String> {
    
    @Query("{user:'?0'}")
    List<ExamTaken> findAllByUser(User user);
    
    @Query("{exam:'?0'}")
    List<ExamTaken> findAllByExam(Exam exam);
    
//    @Query("{submissionDate:'?0'}")
//    Optional<List<ExamTaken>> findAllBySubmissionDate(LocalDateTime submissionDate);
    
    //Optional<List<ExamTaken>> findByPassed(boolean isPassed);
}
