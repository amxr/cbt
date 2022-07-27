package com.fip.cbt.repository;

import com.fip.cbt.model.Exam;
import com.fip.cbt.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    void deleteAllByExam(Exam exam);
}
