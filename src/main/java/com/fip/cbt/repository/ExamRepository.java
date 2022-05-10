package com.fip.cbt.repository;

import com.fip.cbt.model.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends MongoRepository<Exam, String> {

    @Query("{examNumber:'?0'}")
    Optional<Exam> findExamByExamNumber(String examNumber);

    long count();
}
