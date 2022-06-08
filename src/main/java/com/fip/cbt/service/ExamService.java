//package com.fip.cbt.service;
//
//import com.fip.cbt.controller.request.ExamRequest;
//import com.fip.cbt.controller.request.UpdateExamRequest;
//import com.fip.cbt.model.Exam;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.List;
//import java.util.Set;
//
//public interface ExamService {
//    Exam add(ExamRequest examRequest, UserDetails userDetails);
//
//    Exam getOne(String examNumber, UserDetails userDetails);
//
//    void delete(String examNumber, UserDetails userDetails);
//
//    Exam update(UpdateExamRequest updateExamRequest, UserDetails userDetails);
//
//    List<Exam> getAll(UserDetails userDetails);
//
//    //Exam addCandidates(String examNumber, AddCandidatesRequest addCandidatesRequest);
//
//    Exam userRegistration(String examNumber, UserDetails userDetails);
//
//    Exam approveCandidates(String examNumber, Set<String> approvedCandidates, UserDetails userDetails);
//}
