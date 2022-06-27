package com.fip.cbt.repository;

import com.fip.cbt.model.QuestionResponse;
import com.fip.cbt.model.TakenExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {
    Set<QuestionResponse> findByTakenExam(TakenExam takenExam);
}
