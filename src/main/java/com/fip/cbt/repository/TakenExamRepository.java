package com.fip.cbt.repository;

import com.fip.cbt.model.Exam;
import com.fip.cbt.model.TakenExam;
import com.fip.cbt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface TakenExamRepository extends JpaRepository<TakenExam, Long> {

    List<TakenExam> findAllByCandidate(User user);

    Optional<TakenExam> findByCandidateAndExam(User candidate, Exam exam);

    List<TakenExam> findAllByExam_Owner(User owner);

    Boolean existsByCandidateAndExam(User candidate, Exam exam);

}
