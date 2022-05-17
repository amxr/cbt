package com.fip.cbt.repository;

import com.fip.cbt.model.Exam;
import com.fip.cbt.model.ExamTaken;
import com.fip.cbt.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExamTakenRepository extends MongoRepository<ExamTaken, String> {
    
    //@Query("{'user._id': ?0}")
    List<ExamTaken> findAllByUserId(User user);
    
    //@Query(value = "{'user.id': ?0}")
    //@Query(value = "{'user.id': ?0}", fields = "{'user' : 0}")
    //@Query(value = "{'user': {$elemMatch: { 'user.id': ?0 }}")
    //List<ExamTaken> findByUserId(String userId);
    
    //@Query("{exam: ?0}")
    List<ExamTaken> findAllByExam(Exam exam);
    
    //@Query("{'user$.id': ?0, 'exam$id': ?1 }")
    Optional<ExamTaken> findOneByUserIdAndExamId(User user, Exam exam);
    
//    @Query("{submissionDate:'?0'}")
//    Optional<List<ExamTaken>> findAllBySubmissionDate(LocalDateTime submissionDate);
    
    //Optional<List<ExamTaken>> findByPassed(boolean isPassed);
}
