package com.fip.cbt.service;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.model.ExamTaken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ExamTakenService {
    ExamTaken add(ExamTakenRequest examTakenRequest, UserDetails userDetails);
    
    ExamTaken getOne(String id);
    
    void delete(String id);
    
    //ExamTaken update(UpdateExamTakenRequest updateExamTakenRequest);
    
    List<ExamTaken> getAll(String user, String exam, UserDetails userDetails);
}
