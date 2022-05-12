package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.mapper.ExamTakenMapper;
import com.fip.cbt.model.*;
import com.fip.cbt.repository.ExamRepository;
import com.fip.cbt.repository.ExamTakenRepository;
import com.fip.cbt.repository.UserRepository;
import com.fip.cbt.service.ExamTakenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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

    @Autowired
    ExamTakenMapper mapper;
    
    @Override
    public ExamTaken add(ExamTakenRequest examTakenRequest, UserDetails userDetails) {
        //TODO: Search by ID
        Exam exam = examRepository.findById(examTakenRequest.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        User user = (User) userDetails;

        AtomicReference<Double> totalScore = new AtomicReference<>((double) 0);
        List<QuestionResponse> responses = examTakenRequest.getResponses()
                .stream().peek(r -> {
                    r.setCorrect(r.getUserChoice().equals(r.getAnswer()));
                    totalScore.updateAndGet(v -> (v += r.isCorrect() ? r.getPoint() : 0));
                })
                .collect(Collectors.toList());

        ExamTaken examTaken = mapper.toExamTaken(examTakenRequest)
                        .setUser(user).setExam(exam)
                                .setResponses(responses)
                                        .setTotalPoints(totalScore.get())
                                                .setPassed(totalScore.get() >= exam.getPassMark());

        return examTakenRepository.save(examTaken);
    }
    
    @Override
    public ExamTaken getOne(String id) {
        return examTakenRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Exam with number " + id + " not found.")
        );
    }
    
    @Override
    public void delete(String id) {
        ExamTaken examTaken = examTakenRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Could not find Exam with id " +id)
        );
        examTakenRepository.delete(examTaken);
    }
    
//    @Override
//    public ExamTaken update(UpdateExamTakenRequest updateExamTakenRequest) {
//        examTakenRepository.findById(updateExamTakenRequest.getId())
//                           .orElseThrow(
//                              () -> new ResourceNotFoundException("Could not find Exam with id "
//                                                                          +updateExamTakenRequest.getId())
//                      );
//        ExamTaken examTaken = ExamTakenMapper.toExamTaken(updateExamTakenRequest);
//        return examTakenRepository.save(examTaken);
//    }
    
    @Override
    public List<ExamTaken> getAll(String user, String exam, UserDetails userDetails) {
        User currentUser = (User) userDetails;
        if(currentUser.getRole().equals(Role.CANDIDATE)){
            return examTakenRepository.findAllByUser(currentUser);
        }
        List<ExamTaken> examTakens = examTakenRepository.findAll();
        if(!user.equals("")){

            User _user = userRepository.findUserByEmail(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));
            return examTakenRepository.findAllByUser(_user);
        }
        if(!exam.equals("")){
            Exam _exam = examRepository.findExamByExamNumber(exam)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam does not exist."));
            return examTakenRepository.findAllByExam(_exam);
        }

        return examTakenRepository.findAll();
    }
}