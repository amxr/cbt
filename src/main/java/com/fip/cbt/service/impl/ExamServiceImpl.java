package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.AddCandidatesRequest;
import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.mapper.ExamMapper;
import com.fip.cbt.model.Exam;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.ExamRepository;
import com.fip.cbt.repository.UserRepository;
import com.fip.cbt.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamRepository examRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Exam add(ExamRequest examRequest) {
        if(examRepository.findExamByExamNumber(examRequest.getExamNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Exam with number " + examRequest.getExamNumber() + " already exists.");

        Set<User> users = examRequest.getCandidates()
                .stream().map(r -> {
                    return userRepository.findUserByEmail(r).orElseThrow(() -> new ResourceNotFoundException("Error adding the candidates for exam"));
                })
                .collect(Collectors.toSet());

        Exam exam = ExamMapper.toExam(examRequest);
        exam.setCandidates(users);
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

    @Override
    public Exam addCandidates(String examNumber, AddCandidatesRequest addCandidatesRequest) {
        Exam exam = examRepository.findExamByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );

        Set<User> candidates = addCandidatesRequest.getCandidates()
                .stream().map(r -> userRepository.findUserByEmail(r)
                        .orElseThrow(() -> new ResourceNotFoundException("Error adding the candidates for exam")))
                .collect(Collectors.toSet());

        exam.getCandidates().addAll(candidates);

        return examRepository.save(exam);
    }
}
