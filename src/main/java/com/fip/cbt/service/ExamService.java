package com.fip.cbt.service;

import com.fip.cbt.controller.request.ExamRequest;
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

    Exam userRegistration(String examNumber);

    Exam approveCandidates(String examNumber, Set<String> approvedCandidates);

    Exam addQuestions(String examNumber, List<Question> questions);
}
