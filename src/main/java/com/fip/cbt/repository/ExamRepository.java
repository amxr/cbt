package com.fip.cbt.repository;

import com.fip.cbt.model.Exam;
import com.fip.cbt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    Optional<Exam> findExamByExamNumber(String examNumber);

    List<Exam> findAllByOwner(User owner);

    List<Exam> findAllByCandidates(User candidate);
}
