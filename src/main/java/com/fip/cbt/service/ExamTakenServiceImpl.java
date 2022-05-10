package com.fip.cbt.service;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.controller.request.UpdateExamTakenRequest;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.mapper.ExamTakenMapper;
import com.fip.cbt.model.ExamTaken;
import com.fip.cbt.repository.ExamTakenRepository;
import com.fip.cbt.repository.QuestionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class ExamTakenServiceImpl implements ExamTakenService {
    
    @Autowired
    private ExamTakenRepository examTakenRepository;
    
    @Autowired
    private QuestionResponseRepository questionResponseRepository;
    
    @Override
    public ExamTaken add(ExamTakenRequest examTakenRequest) {
        //TODO: Search by ID
        if(examTakenRepository.findByExamId(examTakenRequest.getExamId()).isPresent())
            throw new ResourceAlreadyExistsException("Exam with number " + examTakenRequest.getExamId()
                                                             + " has already been taken and saved.");
    
        ExamTaken examTaken = ExamTakenMapper.toExamTaken(examTakenRequest);
        examTakenRequest.setSubmissionDate(LocalDateTime.now());
    
        return examTakenRepository.save(examTaken);
    }
    
    @Override
    public ExamTaken getOne(String examId) {
        return examTakenRepository.findByExamId(examId).orElseThrow(
                () -> new ResourceNotFoundException("Exam with number " + examId + " not found.")
        );
    }
    
    @Override
    public void delete(String examId) {
        ExamTaken examTaken = examTakenRepository.findByExamId(examId).orElseThrow(
                () -> new ResourceNotFoundException("Could not find Exam with id " +examId)
        );
        examTakenRepository.delete(examTaken);
    }
    
    @Override
    public ExamTaken update(UpdateExamTakenRequest updateExamTakenRequest) {
        examTakenRepository.findById(updateExamTakenRequest.getId())
                           .orElseThrow(
                              () -> new ResourceNotFoundException("Could not find Exam with id "
                                                                          +updateExamTakenRequest.getId())
                      );
        ExamTaken examTaken = ExamTakenMapper.toExamTaken(updateExamTakenRequest);
        return examTakenRepository.save(examTaken);
    }
    
    @Override
    public List<ExamTaken> getAll() {
        return examTakenRepository.findAll();
    }
}
