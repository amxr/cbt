//package com.fip.cbt.repository;
//
//import com.fip.cbt.model.QuestionResponse;
//import com.fip.cbt.model.ExamTaken;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataMongoTest
//@ExtendWith(MockitoExtension.class)
//public class ExamTakenRepositoryTest {
//    @Autowired
//    ExamTakenRepository examTakenRepository;
//
//    @BeforeEach
//    public void setUp(){
//        ExamTaken ExamTakenHistory = new ExamTaken()
//                .setExamId("hist101")
//                .setUsername("aalex")
//                .setTotalPoints(100)
//                .setSubmissionDate(LocalDateTime.of(2022,5,10,20,35))
//                .setResponses(getResponses());
//
//        ExamTaken ExamTakenGov = new ExamTaken()
//                .setExamId("gov101")
//                .setUsername("bobbyr")
//                .setTotalPoints(100)
//                .setSubmissionDate(LocalDateTime.of(2022,5,10,20,35))
//                .setResponses(getResponses());
//        examTakenRepository.saveAll(List.of(ExamTakenHistory,ExamTakenGov));
//    }
//
//    @Test
//    public void createAndDeleteTestsTaken(){
//
//        List<ExamTaken> takenTests = examTakenRepository.findAll();
//
//        //then
//        assertThat(takenTests.size()).isEqualTo(2);
//
//        //when
//        examTakenRepository.deleteAll();
//        List<ExamTaken> tests1 = examTakenRepository.findAll();
//
//        //then
//        assertThat(tests1.size()).isEqualTo(0);
//    }
//    @Test
//    public void findByUsernameTest(){
//        assertThat(examTakenRepository.count()).isEqualTo(2);
//
//        Optional<ExamTaken> testAlice = examTakenRepository.findByCandidate("aalex");
//
//        assertThat(testAlice.get().getUsername()).isEqualTo("aalex");
//
//        Optional<ExamTaken> doesNotExist = examTakenRepository.findByCandidate("alex");
//
//        assertThat(doesNotExist).isEmpty();
//    }
//    @AfterEach
//    public void tearDown(){
//        examTakenRepository.deleteAll();
//    }
//
//    private List<QuestionResponse> getResponses(){
//        return List.of(
//                new QuestionResponse()
//                        .setUserChoice("1964")
//                        .setCorrect(false)
//                        .setText("What year did Nigeria get its independence?")
//                        .setAnswer("1960")
//                        .setOptions(List.of("1959", "1960", "1961", "1962"))
//                        .setPoint(2.0),
//
//                new QuestionResponse()
//                        .setUserChoice("1964")
//                        .setCorrect(true)
//                        .setText("What year did Nigeria change its currency?")
//                        .setAnswer("1964")
//                        .setOptions(List.of("1961", "1962", "1963", "1964"))
//                        .setPoint(1.5)
//        );
//    }
//
//}
