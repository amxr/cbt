package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.dto.mapper.ExamTakenMapper;
import com.fip.cbt.model.*;
import com.fip.cbt.repository.ExamRepository;
import com.fip.cbt.repository.ExamTakenRepository;
import com.fip.cbt.repository.UserRepository;
import com.fip.cbt.service.ExamTakenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    private UserService userService;
    
    @Override
    public ExamTaken add(ExamTakenRequest examTakenRequest) {
        User user = userService.getUser();
       
        Exam exam = examRepository.findById(examTakenRequest.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        if(!exam.getCandidates().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User has not registered or has not been approved.");
        }
        
        if(examTakenRepository.findOneByUserAndExam(user, exam).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can't submit an exam more than once");
        }

        double totalScore = 0;

        for(QuestionResponse response: examTakenRequest.getResponses()){
            response.setCorrect(response.getUserChoice().equals(response.getAnswer()));
            totalScore += response.isCorrect() ? response.getPoint() : 0;
        }


        ExamTaken examTaken = ExamTakenMapper.toExamTaken(examTakenRequest)
                        .setUser(user).setExam(exam)
                                .setResponses(examTakenRequest.getResponses())
                                        .setTotalPoints(totalScore)
                                                .setPassed(totalScore >= exam.getPassMark());

        return examTakenRepository.save(examTaken);
    }
    
    @Override
    public ExamTaken getOne(String id) {
        User user = userService.getUser();

        ExamTaken taken = examTakenRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Exam with number " + id + " not found.")
        );

        if(user.getRole() == Role.TESTOWNER && (!taken.getExam().getOwner().equals(user))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
        }

        return taken;
    }
    
    @Override
    public void delete(String id) {
        User user = userService.getUser();

        ExamTaken taken = examTakenRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Exam with number " + id + " not found.")
        );
        if(!taken.getExam().getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
        }
        examTakenRepository.delete(taken);
    }
    
    @Override
    public List<ExamTaken> getAll(String examNumber) {
        User user = userService.getUser();

        if(user.getRole().equals(Role.TESTOWNER)){
            return examTakenRepository.findAll()
                    .stream()
                    .filter(e -> {
                        if(!examNumber.isEmpty()){
                            return e.getExam().getOwner().equals(user) && e.getExam().getExamNumber().equals(examNumber);
                        }
                        return e.getExam().getOwner().equals(user);
                    })
                    .collect(Collectors.toList());
        }

        return examTakenRepository.findAllByUser(user);
    }

}
