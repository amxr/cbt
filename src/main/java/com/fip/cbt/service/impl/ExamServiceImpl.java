package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.dto.mapper.ExamMapper;
import com.fip.cbt.model.Exam;
import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.ExamRepository;
import com.fip.cbt.repository.UserRepository;
import com.fip.cbt.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Exam add(ExamRequest examRequest) {
        User user = userService.getUser();

        if(examRepository.findByExamNumber(examRequest.getExamNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Exam with number " + examRequest.getExamNumber() + " already exists.");

        Exam exam = ExamMapper.toExam(examRequest);
        exam
                .setOwner(user)
                .setRegisteredCandidates(Collections.emptySet())
                .setCandidates(Collections.emptySet());

        return examRepository.save(exam);
    }

    @Override
    public Exam getOne(String examNumber) {
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );
        if(user.getRole().equals(Role.TESTOWNER)){
            if(!exam.getOwner().equals(user)){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
            }
        }

        if(user.getRole().equals(Role.CANDIDATE)){
            if(LocalDateTime.now().isBefore(exam.getStart())){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied before scheduled time.");
            }
            if(!exam.getCandidates().contains(user)){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to access this exam");
            }
        }

        return exam;
    }

    @Override
    public void delete(String examNumber) {
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );
        if(!exam.getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
        }
        examRepository.delete(exam);
    }

    @Override
    public Exam update(String examNumber, ExamRequest updateExamRequest) {
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam not found.")
                );

        if(!exam.getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
        }

        if(!updateExamRequest.getExamNumber().equals(examNumber) && examRepository.existsByExamNumber(updateExamRequest.getExamNumber())){
            throw new ResourceAlreadyExistsException("Exam with number: " + updateExamRequest.getExamNumber() + " already exists.");
        }

        ExamMapper.updateExam(exam, updateExamRequest);
        return examRepository.save(exam);
    }

    @Override
    public List<Exam> getAll() {
        User user = userService.getUser();

        if(user.getRole() == Role.CANDIDATE){
            return examRepository.findAllByCandidates(user);
        }

        if(user.getRole() == Role.TESTOWNER){
            return examRepository.findAllByOwner(user);
        }

        return examRepository.findAll();
    }
    
    @Override
    public Exam userRegistration(String examNumber){
        Exam exam = examRepository.findByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );

        User user = userService.getUser();

        if(exam.getRegisteredCandidates() == null){
            exam.setRegisteredCandidates(Set.of(user));
        }else{
            exam.getRegisteredCandidates().add(user);
        }
        
        return examRepository.save(exam);
    }
    
    public Exam approveCandidates(String examNumber, Set<String> approvedCandidates){
        Exam exam = examRepository.findByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );

        User user = userService.getUser();

        if(!exam.getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
        }

        Set<User> candidates = approvedCandidates.stream().map(c -> userRepository.findUserByEmail(c)
                .orElseThrow(() -> new ResourceNotFoundException("No such user: "+c))).collect(Collectors.toSet());

        if(exam.getCandidates() == null){
            exam.setCandidates(candidates);
        }else {
            exam.getCandidates().addAll(candidates);
        }

        exam.getRegisteredCandidates().removeAll(candidates);

        return exam;
    }
}
