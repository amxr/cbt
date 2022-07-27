package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.QuestionResponseRequest;
import com.fip.cbt.controller.request.TakenExamRequest;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.model.*;
import com.fip.cbt.repository.*;
import com.fip.cbt.service.TakenExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TakenExamServiceImpl implements TakenExamService {

    @Autowired
    private TakenExamRepository takenExamRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionResponseRepository questionResponseRepository;

    @Override
    public TakenExam submit(TakenExamRequest takenExamRequest) {
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumberIgnoreCaseAndCandidates(takenExamRequest.getExamNumber(), user)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "User has not registered or has not been approved.")
                );

        if(takenExamRepository.existsByCandidateAndExam(user, exam)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can't submit an exam more than once");
        }

        if(exam.getQuestions().size() != takenExamRequest.getResponses().size()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing questions [size of responses does not match available questions].");
        }

        TakenExam takenExam = new TakenExam()
                .setCandidate(user)
                .setExam(exam)
                .setUserStartTime(takenExamRequest.getUserStartTime());

        takenExam = takenExamRepository.save(takenExam);

        double totalScore = 0;

        for(QuestionResponseRequest qr : takenExamRequest.getResponses()){
            Question question = questionRepository.findById(qr.getQuestionId())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid question id")
                    );

            boolean checkAnswer = qr.getUserChoice().equals(question.getAnswer());

            totalScore += checkAnswer ?
                    question.getPoint() : 0;

            QuestionResponse response = new QuestionResponse()
                    .setUserChoice(qr.getUserChoice())
                    .setTakenExam(takenExam)
                    .setQuestion(question)
                    .setCorrect(checkAnswer);

            questionResponseRepository.save(response);
        }

        takenExam
                .setTotalPoints(totalScore)
                .setPassed(totalScore >= exam.getPassMark());

        takenExamRepository.save(takenExam);

        Set<QuestionResponse> responseSet = questionResponseRepository.findByTakenExam(takenExam);
        return takenExam.setResponses(responseSet);
    }

    @Override
    public TakenExam getOne(String examNumber) {
        User user = userService.getUser();

        Exam exam = examRepository.findByExamNumberIgnoreCase(examNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exam with exam number: " + examNumber+ " not found.")
                );

        return takenExamRepository.findByCandidateAndExam(user, exam)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User has not taken this exam."));
    }

    @Override
    public void delete(Long id) {
        User user = userService.getUser();
        TakenExam takenExam = takenExamRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Invalid id."));

        if(!takenExam.getExam().getOwner().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials unauthorised for such action");
        }

        takenExamRepository.delete(takenExam);
    }

    @Override
    public List<TakenExam> getAll() {
        User user = userService.getUser();

        if(user.getRole().equals(Role.CANDIDATE)){
            return takenExamRepository.findAllByCandidate(user);
        }

        return takenExamRepository.findAllByExam_Owner(user);
    }
}
