package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
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
import org.springframework.security.core.userdetails.UserDetails;
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


    @Override
    public void add(ExamRequest examRequest, UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));

        if(examRepository.findExamByExamNumber(examRequest.getExamNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Exam with number " + examRequest.getExamNumber() + " already exists.");

        Exam exam = ExamMapper.toExam(examRequest);
        exam.setOwner(user);
        Exam savedExam = examRepository.save(exam);

        List<Question> questions = examRequest.getQuestions()
                .stream().map(q -> q.setExam(savedExam))
                .collect(Collectors.toList());

        List<Question> savedQuestions = questionRepository.saveAll(questions);
    }

    @Override
    public Exam getOne(String examNumber, UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));

        Exam exam = examRepository.findExamByExamNumber(examNumber)
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
    public void delete(String examNumber, UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));

        Exam exam = examRepository.findExamByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );
        if(!exam.getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
        }
        examRepository.delete(exam);
    }

    @Override
    public Exam update(UpdateExamRequest updateExamRequest, UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));

        Exam _exam = examRepository.findById(updateExamRequest.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam not found.")
                );

        if(!_exam.getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
        }

        Exam exam = ExamMapper.toExam(updateExamRequest);
        return examRepository.save(exam);
    }

    @Override
    public List<Exam> getAll(UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));

        System.out.println(user);
        if(user.getRole() == Role.CANDIDATE){
            return examRepository.findAllByCandidates(user);
        }

        if(user.getRole() == Role.TESTOWNER){
            return examRepository.findAllByOwner(user);
        }

        return examRepository.findAll();
    }

    @Override
    public Exam userRegistration(String examNumber, UserDetails userDetails){
        Exam exam = examRepository.findExamByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );

        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));

        if(exam.getRegisteredCandidates() == null){
            exam.setRegisteredCandidates(Set.of(user));
        }else{
            exam.getRegisteredCandidates().add(user);
        }

        return examRepository.save(exam);
    }

    public Exam approveCandidates(String examNumber, Set<String> approvedCandidates, UserDetails userDetails){
        Exam exam = examRepository.findExamByExamNumber(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with number " + examNumber + " not found.")
                );

        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));

        if(!exam.getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
        }

        Set<User> candidates = approvedCandidates.stream().map(c -> {
            return userRepository.findUserByEmail(c)
                    .orElseThrow(() -> new ResourceNotFoundException("No such user: "+c));
        }).collect(Collectors.toSet());

        if(exam.getCandidates() == null){
            exam.setCandidates(candidates);
        }else {
            exam.getCandidates().addAll(candidates);
        }

        exam.getRegisteredCandidates().removeAll(candidates);

        return exam;
    }
}
