package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.dto.mapper.ExamMapper;
import com.fip.cbt.model.Exam;
import com.fip.cbt.model.Question;
import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.ExamRepository;
import com.fip.cbt.repository.QuestionRepository;
import com.fip.cbt.repository.UserRepository;
import com.fip.cbt.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamRepository examRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    private UserService userService;


    @Override
    public void add(ExamRequest examRequest) {
        User user = userService.getUser();

        if(examRepository.findByExamNumberIgnoreCase(examRequest.getExamNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Exam with number " + examRequest.getExamNumber() + " already exists.");

        Exam exam = ExamMapper.toExam(examRequest);
        exam.setOwner(user);
        examRepository.save(exam);
    }

    @Override
    public Exam getOne(String examNumber) {
        User user = userService.getUser();

        if(user.getRole().equals(Role.TESTOWNER)){
            return examRepository.findByExamNumberIgnoreCaseAndOwner(examNumber, user)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                    );
        }

        Exam exam = new Exam();
        if(user.getRole().equals(Role.CANDIDATE)){
            exam = examRepository.findByExamNumberIgnoreCaseAndCandidates(examNumber, user)
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to access this exam")
                    );

            if(LocalDateTime.now().isBefore(exam.getStart())){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied before scheduled time.");
            }
        }

        return exam;
    }

    @Override
    public void delete(String examNumber) {
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumberIgnoreCaseAndOwner(examNumber, user)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found."));
        examRepository.delete(exam);
    }

    @Override
    public Exam update(String examNumber, ExamRequest updateExamRequest) {
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumberIgnoreCaseAndOwner(examNumber, user)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found."));

        if(!updateExamRequest.getExamNumber().equals(examNumber) && examRepository.existsByExamNumberIgnoreCase(updateExamRequest.getExamNumber())){
            throw new ResourceAlreadyExistsException("Exam with number: " + updateExamRequest.getExamNumber() + " already exists.");
        }

        ExamMapper.updateExam(exam, updateExamRequest);
        return examRepository.save(exam);
    }

    @Override
    public List<Exam> getAll(Optional<Boolean> approved) {
        User user = userService.getUser();

        System.out.println(user);
        if(user.getRole() == Role.CANDIDATE){
            if(approved.orElse(false))
                return examRepository.findAllByCandidates(user);
            else
                return examRepository.findAllByRegisteredCandidates(user);
        }

        if(user.getRole() == Role.TESTOWNER){
            return examRepository.findAllByOwner(user);
        }

        return examRepository.findAll();
    }

    @Override
    public Exam userRegistration(String examNumber){
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumberIgnoreCase(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found."));
        if(exam.getRegisteredCandidates() == null){
            exam.setRegisteredCandidates(Set.of(user));
        }else{
            exam.getRegisteredCandidates().add(user);
        }

        return examRepository.save(exam);
    }

    public Exam approveCandidates(String examNumber, Set<String> approvedCandidates){
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumberIgnoreCaseAndOwner(examNumber, user)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found."));

        Set<User> candidates = approvedCandidates.stream().map(c ->
                userRepository.findUserByEmailIgnoreCase(c)
                    .orElseThrow(() -> new ResourceNotFoundException("No such user: "+c))
        ).collect(Collectors.toSet());

        if(exam.getCandidates() == null){
            exam.setCandidates(candidates);
        }else {
            exam.getCandidates().addAll(candidates);
        }

        exam.getRegisteredCandidates().removeAll(candidates);

        return exam;
    }

    @Override
    public Exam addQuestions(String examNumber, List<Question> questions) {
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumberIgnoreCaseAndOwner(examNumber, user)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found."));

        for(Question question : questions){
            question.setExam(exam);
            questionRepository.save(question);
        }

        return examRepository.getById(exam.getId());
    }
}
