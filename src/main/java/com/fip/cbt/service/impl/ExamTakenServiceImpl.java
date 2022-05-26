package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.dto.mapper.ExamTakenMapper;
import com.fip.cbt.model.*;
import com.fip.cbt.repository.ExamRepository;
import com.fip.cbt.repository.ExamTakenRepository;
import com.fip.cbt.repository.UserRepository;
import com.fip.cbt.service.ExamTakenService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ExamTakenServiceImpl implements ExamTakenService {
    
    @Autowired
    private ExamTakenRepository examTakenRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public ExamTaken add(ExamTakenRequest examTakenRequest, UserDetails userDetails) {
        //TODO: Search by ID [DONE]
        //TODO: Refactor the following lines
        Exam exam = examRepository.findById(examTakenRequest.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        
        User user = getUser(userDetails.getUsername());

        if(!exam.getCandidates().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to take this exam.");
        }
        
        if(examTakenRepository.findOneByUserAndExam(user, exam).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can't submit an exam more than once");
        }
        //TODO: End of proposed Refactoring
        
        AtomicReference<Double> totalScore = new AtomicReference<>((double) 0);
        List<QuestionResponse> responses = examTakenRequest.getResponses()
                .stream().peek(r -> {
                    r.setCorrect(r.getUserChoice().equals(r.getAnswer()));
                    totalScore.updateAndGet(v -> (v += r.isCorrect() ? r.getPoint() : 0));
                })
                .collect(Collectors.toList());

        ExamTaken examTaken = ExamTakenMapper.toExamTaken(examTakenRequest)
                        .setUser(user).setExam(exam)
                                .setResponses(responses)
                                        .setTotalPoints(totalScore.get())
                                                .setPassed(totalScore.get() >= exam.getPassMark());

        return examTakenRepository.save(examTaken);
    }
    
    @Override
    public ExamTaken getOne(String id, UserDetails userDetails) {
        User user = getUser(userDetails.getUsername());

        ExamTaken taken = examTakenRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Exam with number " + id + " not found.")
        );

        if(user.getRole() == Role.TESTOWNER && !taken.getExam().getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This exam does not belong to you.");
        }

        return taken;
    }
    
    @Override
    public void delete(String id, UserDetails userDetails) {
        User user = getUser(userDetails.getUsername());

        ExamTaken taken = examTakenRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Exam with number " + id + " not found.")
        );
        if(!taken.getExam().getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This exam does not belong to you.");
        }
        examTakenRepository.delete(taken);
    }
    
    @Override
    public List<ExamTaken> getAll(UserDetails userDetails) {
        User user = getUser(userDetails.getUsername());

        if(user.getRole().equals(Role.CANDIDATE)){
            return examTakenRepository.findAllByUser(user);
        }

        if(user.getRole().equals(Role.TESTOWNER)){
            return examTakenRepository.findAll()
                    .stream()
                    .filter(e -> e.getExam().getOwner().equals(user))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public User getUser(String email){
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No such User: "+email));
    }
}
