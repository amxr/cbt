package com.fip.cbt.service;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.dto.ScheduledExam;
import com.fip.cbt.model.Exam;
import com.fip.cbt.model.Question;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ExamService {
    void add(ExamRequest examRequest);

    Exam getOne(String examNumber);

    void delete(String examNumber);

    Exam update(String examNumber, ExamRequest updateExamRequest);

    List<Exam> getAll(Optional<Boolean> approved);

    //Exam addCandidates(String examNumber, AddCandidatesRequest addCandidatesRequest);

    void userRegistration(String examNumber);

    void approveCandidates(String examNumber, Set<String> approvedCandidates);

    void addQuestions(String examNumber, List<Question> questions);

    List<ScheduledExam> getScheduledExams();
}
