//package com.fip.cbt.repository;
//
//import com.fip.cbt.model.Exam;
//import com.fip.cbt.model.User;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface ExamRepository extends MongoRepository<Exam, String> {
//
//    Optional<Exam> findExamByExamNumber(String examNumber);
//
//    List<Exam> findAllByOwner(User owner);
//
//    List<Exam> findAllByCandidates(User candidate);
//
//    List<Exam> findExamByStart(LocalDateTime start);
//   //TODO: Make query more robust
//
//    long count();
//}
