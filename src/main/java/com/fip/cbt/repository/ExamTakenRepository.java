//package com.fip.cbt.repository;
//
//import com.fip.cbt.model.Exam;
//import com.fip.cbt.model.ExamTaken;
//import com.fip.cbt.model.User;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.core.aggregation.AggregationResults;
//import org.springframework.data.mongodb.core.aggregation.MatchOperation;
//import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.repository.Aggregation;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//public interface ExamTakenRepository extends MongoRepository<ExamTaken, String> {
//
//    List<ExamTaken> findAllByUser(User user);
//
//    List<ExamTaken> findAllByExam(Exam exam);
//
//    Optional<ExamTaken> findOneByUserAndExam(User user, Exam exam);
//
////    @Query("{submissionDate:'?0'}")
////    Optional<List<ExamTaken>> findAllBySubmissionDate(LocalDateTime submissionDate);
//}
