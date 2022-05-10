package com.fip.cbt.service;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.controller.request.UpdateExamTakenRequest;
import com.fip.cbt.model.ExamTaken;

import java.util.List;

public interface ExamTakenService {
    ExamTaken add(ExamTakenRequest examTakenRequest);
    
    ExamTaken getOne(String id);
    
    void delete(String id);
    
    ExamTaken update(UpdateExamTakenRequest updateExamTakenRequest);
    
    List<ExamTaken> getAll();
}
