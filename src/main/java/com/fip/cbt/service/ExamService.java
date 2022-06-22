package com.fip.cbt.service;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.model.Exam;

import java.util.List;
import java.util.Set;

public interface ExamService {
    Exam add(ExamRequest examRequest);

    Exam getOne(String examNumber);

    void delete(String examNumber);

    Exam update(String examNumber, ExamRequest updateExamRequest);

    List<Exam> getAll();
    
    Exam userRegistration(String examNumber);
    
    Exam approveCandidates(String examNumber, Set<String> approvedCandidates);
}
