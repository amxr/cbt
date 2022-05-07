package com.fip.cbt.service;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.mapper.ExamMapper;
import com.fip.cbt.model.Exam;
import com.fip.cbt.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamRepository examRepository;

    @Override
    public Exam add(ExamRequest examRequest) {
        if(examRepository.findExamByExamNumber(examRequest.getExamNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Exam with number " + examRequest.getExamNumber() + " already exists.");

        Exam exam = ExamMapper.toExam(examRequest);
        exam.setCreated(LocalDateTime.now());

        return examRepository.save(exam);
    }

    @Override
    public Exam getOne(String examNumber) {
        return examRepository.findExamByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );
    }

    @Override
    public void delete(String examNumber) {
        Exam exam = examRepository.findExamByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );
        examRepository.delete(exam);
    }

    @Override
    public Exam update(UpdateExamRequest updateExamRequest) {
        examRepository.findById(updateExamRequest.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam not found.")
                );
        Exam exam = ExamMapper.toExam(updateExamRequest);
        return examRepository.save(exam);
    }

    @Override
    public List<Exam> getAll() {
        return examRepository.findAll();
    }
}
