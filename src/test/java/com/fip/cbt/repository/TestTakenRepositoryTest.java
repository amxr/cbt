package com.fip.cbt.repository;

import com.fip.cbt.controller.request.TestTakenRequest;
import com.fip.cbt.mapper.TestTakenMapper;
import com.fip.cbt.model.Candidate;
import com.fip.cbt.model.QuestionResponse;
import com.fip.cbt.model.TestTaken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
public class TestTakenRepositoryTest {
    @Autowired
    TestTakenRepository testTakenRepository;
    
    @BeforeEach
    public void setUp(){
        TestTaken testTakenHistory = new TestTaken()
                .setTestId("hist101")
                .setUsername("aalex")
                .setTotalPoints(100)
                .setSubmissionDate(LocalDateTime.of(2022,5,10,20,35))
                .setResponseSet(new HashSet<>(){
                    {
                        add(QuestionResponse
                                    .builder()
                                    .text("What year did Nigeria get its independence?")
                                    .answer("1960")
                                    .options(new ArrayList<>() {
                                        {
                                            add("1959");
                                            add("1960");
                                            add("1961");
                                            add("1962");
                                        }
                                    })
                                    .point(2.0)
                                    .userChoice("1964")
                                    .isCorrect(false)
                                    .build());
                        add(QuestionResponse
                                    .builder()
                                    .text("What year did Nigeria change its currency?")
                                    .answer("1970")
                                    .options(new ArrayList<>(){
                                        {
                                            add("1960");
                                            add("1961");
                                            add("1962");
                                            add("1963");
                                        }
                                    })
                                    .point(1.5)
                                    .userChoice("1964")
                                    .isCorrect(false)
                                    .build());
                    }
                });
        TestTaken testTakenGov = new TestTaken()
                .setTestId("gov101")
                .setUsername("bobbyr")
                .setTotalPoints(100)
                .setSubmissionDate(LocalDateTime.of(2022,5,10,20,35))
                .setResponseSet(new HashSet<>(){
                    {
                        add(QuestionResponse
                                    .builder()
                                    .text("What year did Nigeria get its independence?")
                                    .answer("1960")
                                    .options(new ArrayList<>() {
                                        {
                                            add("1959");
                                            add("1960");
                                            add("1961");
                                            add("1962");
                                        }
                                    })
                                    .point(2.0)
                                    .userChoice("1964")
                                    .isCorrect(false)
                                    .build());
                        add(QuestionResponse
                                    .builder()
                                    .text("What year did Nigeria change its currency?")
                                    .answer("1970")
                                    .options(new ArrayList<>(){
                                        {
                                            add("1960");
                                            add("1961");
                                            add("1962");
                                            add("1963");
                                        }
                                    })
                                    .point(1.5)
                                    .userChoice("1964")
                                    .isCorrect(false)
                                    .build());
                    }
                });
        testTakenRepository.saveAll(List.of(testTakenHistory,testTakenGov));
    }
    
    @Test
    public void createAndDeleteTestsTaken(){
        
        List<TestTaken> takenTests = testTakenRepository.findAll();
        
        //then
        assertThat(takenTests.size()).isEqualTo(2);
        
        //when
        testTakenRepository.deleteAll();
        List<TestTaken> tests1 = testTakenRepository.findAll();
        
        //then
        assertThat(tests1.size()).isEqualTo(0);
    }
    @Test
    public void findByUsernameTest(){
        assertThat(testTakenRepository.count()).isEqualTo(2);
        
        Optional<TestTaken> testAlice = testTakenRepository.findByCandidate("aalex");
        
        assertThat(testAlice.get().getUsername()).isEqualTo("aalex");
        
        Optional<TestTaken> doesNotExist = testTakenRepository.findByCandidate("alex");
        
        assertThat(doesNotExist).isEmpty();
    }
    @AfterEach
    public void tearDown(){
        testTakenRepository.deleteAll();
    }
 
}
