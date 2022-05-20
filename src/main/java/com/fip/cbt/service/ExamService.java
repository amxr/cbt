package com.fip.cbt.service;

import com.fip.cbt.controller.request.AddCandidatesRequest;
import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
import com.fip.cbt.model.Exam;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ExamService {
    Exam add(ExamRequest examRequest);

    Exam getOne(String examNumber);

    void delete(String examNumber);

    Exam update(UpdateExamRequest updateExamRequest);

    List<Exam> getAll();

    Exam addCandidates(String examNumber, AddCandidatesRequest addCandidatesRequest);
    
    Exam registerUser(String examNumber, UserDetails userDetails);
    
    Exam approveCandidates(String examNumber, AddCandidatesRequest approvedCandidates);
}
