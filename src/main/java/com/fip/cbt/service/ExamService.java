package com.fip.cbt.service;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
import com.fip.cbt.model.Exam;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

public interface ExamService {
    Exam add(ExamRequest examRequest);

    Exam getOne(String examNumber);

    void delete(String examNumber);

    Exam update(UpdateExamRequest updateExamRequest);

    List<Exam> getAll();

    //Exam addCandidates(String examNumber, AddCandidatesRequest addCandidatesRequest);
    
    Exam userRegistration(String examNumber, UserDetails userDetails);
    
    Exam approveCandidates(String examNumber, Set<String> approvedCandidates);
}
