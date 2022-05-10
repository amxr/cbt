package com.fip.cbt.repository;

import com.fip.cbt.model.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ExamRepository extends MongoRepository<Exam, String> {

    @Query("{examNumber:'?0'}")
    Optional<Exam> findExamByExamNumber(String examNumber);

   @Query("{name:'?0'}")
    Optional<Exam> findExamByName(String name);

   @Query("{created:'?0'}")
    Optional<Exam> findExamByDate(LocalDateTime created);
   //TODO: Make query more robust

   @Query("{duration:'?0'}")
    Optional<Exam> findExamByDuration(int duration);

    long count();
}
