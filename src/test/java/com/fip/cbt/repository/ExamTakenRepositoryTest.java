package com.fip.cbt.repository;

import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.dto.mapper.ExamMapper;
import com.fip.cbt.dto.mapper.ExamTakenMapper;
import com.fip.cbt.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
public class ExamTakenRepositoryTest {
    @Autowired
    ExamTakenRepository examTakenRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ExamRepository examRepository;
    
    @AfterEach
    void tearDown(){
        examTakenRepository.deleteAll();
        userRepository.deleteAll();
        examRepository.deleteAll();
    }
    
    @BeforeEach
    void setUp(){
        Exam examN101 = ExamMapper.toExam(getExam("N101", List.of("aalex@cbt.com")));
        Exam examN102 = ExamMapper.toExam(getExam("N102",List.of("aalex@cbt.com","bobreed@cbt.com")));
        examRepository.saveAll(List.of(examN101, examN102));
        
        examTakenRepository.saveAll(List.of(
                new ExamTaken()
                        .setExam(examN101)
                        .setResponses(new ArrayList<>(){{
                            add(new QuestionResponse().setUserChoice("Me")
                                                      .setText("How are you?")
                                                      .setAnswer("Me")
                                                      .setOptions(List.of("Hello", "Me", "You", "Him"))
                                                      .setPoint(5)
                            );
                            add(new QuestionResponse().setUserChoice("Him")
                                                      .setText("Who is she?")
                                                      .setAnswer("Her")
                                                      .setOptions(List.of("Her", "Him", "They", "Them"))
                                                      .setPoint(3)
                            );
                            add(new QuestionResponse().setUserChoice("it")
                                                      .setText("Who is he?")
                                                      .setAnswer("It")
                                                      .setOptions(List.of("You", "Me", "Them", "It"))
                                                      .setPoint(2)
                            );
                        }})
                        .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0)),
                new ExamTaken()
                        .setExam(examN102)
                        .setResponses(new ArrayList<>(){{
                            add(new QuestionResponse().setUserChoice("Me")
                                                      .setText("How are you?")
                                                      .setAnswer("Me")
                                                      .setOptions(List.of("Hello", "Me", "You", "Him"))
                                                      .setPoint(5)
                            );
                            add(new QuestionResponse().setUserChoice("Him")
                                                      .setText("Who is she?")
                                                      .setAnswer("Her")
                                                      .setOptions(List.of("Her", "Him", "They", "Them"))
                                                      .setPoint(3)
                            );
                            add(new QuestionResponse().setUserChoice("it")
                                                      .setText("Who is he?")
                                                      .setAnswer("It")
                                                      .setOptions(List.of("You", "Me", "Them", "It"))
                                                      .setPoint(2)
                            );
                        }})
                        .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0))
        ));
        userRepository.saveAll(List.of(
                new User().setName("Alice Alex")
                          .setEmail("aalex@cbt.com")
                          .setPassword("aliceAlex123")
                          .setRole(Role.CANDIDATE),
                new User().setName("Robert Reed")
                             .setEmail("bobreed@cbt.com")
                             .setPassword("bobbyreeder1")
                             .setRole(Role.CANDIDATE)
                ));
    }
    
    @Test
    public void findAllByUserIdTest(){
        User alice = userRepository.findUserByEmail("aalex@cbt.com")
                .orElseThrow( ()-> new ResourceNotFoundException("No such user [aalex@cbt.com]"));
        
        Exam exam1 = examRepository.findExamByExamNumber("N101")
                .orElseThrow(()->new ResourceNotFoundException("No such exam"));
        Exam exam2 = examRepository.findExamByExamNumber("N102")
                .orElseThrow(()->new ResourceNotFoundException("No such exam"));
        
        ExamTakenRequest examTakenRequest1 = new ExamTakenRequest()
                .setExamId(exam1.getId())
                .setResponses(new ArrayList<>(){{
                    add(new QuestionResponse().setUserChoice("Me")
                                              .setText("How are you?")
                                              .setAnswer("Me")
                                              .setOptions(List.of("Hello", "Me", "You", "Him"))
                                              .setPoint(5)
                    );
                    add(new QuestionResponse().setUserChoice("Him")
                                              .setText("Who is she?")
                                              .setAnswer("Her")
                                              .setOptions(List.of("Her", "Him", "They", "Them"))
                                              .setPoint(3)
                    );
                    add(new QuestionResponse().setUserChoice("it")
                                              .setText("Who is he?")
                                              .setAnswer("It")
                                              .setOptions(List.of("You", "Me", "Them", "It"))
                                              .setPoint(2)
                    );
                }})
                .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0));
        ExamTakenRequest examTakenRequest2 = new ExamTakenRequest()
                .setExamId(exam2.getId())
                .setResponses(new ArrayList<>(){{
                    add(new QuestionResponse().setUserChoice("Me")
                                              .setText("How are you?")
                                              .setAnswer("Me")
                                              .setOptions(List.of("Hello", "Me", "You", "Him"))
                                              .setPoint(5)
                    );
                    add(new QuestionResponse().setUserChoice("Him")
                                              .setText("Who is she?")
                                              .setAnswer("Her")
                                              .setOptions(List.of("Her", "Him", "They", "Them"))
                                              .setPoint(3)
                    );
                    add(new QuestionResponse().setUserChoice("it")
                                              .setText("Who is he?")
                                              .setAnswer("It")
                                              .setOptions(List.of("You", "Me", "Them", "It"))
                                              .setPoint(2)
                    );
                }})
                .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0));
        
        ExamTaken ignored1 = examTakenRepository.save(
                ExamTakenMapper.toExamTaken(examTakenRequest1)
                        .setExam(exam1)
                        .setUser(alice)
                        .setPassed(true)
                        .setTotalPoints(75.0)
        );
        ExamTaken ignored2 = examTakenRepository.save(
                ExamTakenMapper.toExamTaken(examTakenRequest1)
                        .setExam(exam2)
                        .setUser(alice)
                        .setPassed(true)
                        .setTotalPoints(75.0)
        );
        List<ExamTaken> examsByUser = examTakenRepository.findAllByUserId(alice);
        assertThat(examsByUser.size()).isEqualTo(2);
    
        User john = userRepository.save(
                new User().setName("John Doe")
                          .setEmail("jdoe@cbt.com")
                          .setPassword("johndoe123")
                          .setRole(Role.CANDIDATE)
        );
        List<ExamTaken> examsByNewUser = examTakenRepository.findAllByUserId(john);
        assertThat(examsByNewUser.size()).isEqualTo(0);
    }
    
    @Test
    public void findAllByExamTest(){
        Exam examN103 = ExamMapper.toExam(getExam("N101",List.of("aalex@cbt.com","bobreed@cbt.com")));
        Exam examN104 = ExamMapper.toExam(getExam("N102",List.of("aalex@cbt.com","bobreed@cbt.com")));
        examRepository.saveAll(List.of(examN103, examN104));
        //TODO: Test for examNumber(if it is unique
        ExamTakenRequest examTakenRequest1 = new ExamTakenRequest()
                .setExamId(examN103.getId())
                        .setResponses(new ArrayList<>(){{
                            add(new QuestionResponse().setUserChoice("Me")
                                                      .setText("How are you?")
                                                      .setAnswer("Me")
                                                      .setOptions(List.of("Hello", "Me", "You", "Him"))
                                                      .setPoint(5)
                            );
                            add(new QuestionResponse().setUserChoice("Him")
                                                      .setText("Who is she?")
                                                      .setAnswer("Her")
                                                      .setOptions(List.of("Her", "Him", "They", "Them"))
                                                      .setPoint(3)
                            );
                            add(new QuestionResponse().setUserChoice("it")
                                                      .setText("Who is he?")
                                                      .setAnswer("It")
                                                      .setOptions(List.of("You", "Me", "Them", "It"))
                                                      .setPoint(2)
                            );
                        }})
                                .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0));
        ExamTakenRequest examTakenRequest2 = new ExamTakenRequest()
                .setExamId(examN104.getId())
                .setResponses(new ArrayList<>(){{
                    add(new QuestionResponse().setUserChoice("Me")
                                              .setText("How are you?")
                                              .setAnswer("Me")
                                              .setOptions(List.of("Hello", "Me", "You", "Him"))
                                              .setPoint(5)
                    );
                    add(new QuestionResponse().setUserChoice("Him")
                                              .setText("Who is she?")
                                              .setAnswer("Her")
                                              .setOptions(List.of("Her", "Him", "They", "Them"))
                                              .setPoint(3)
                    );
                    add(new QuestionResponse().setUserChoice("it")
                                              .setText("Who is he?")
                                              .setAnswer("It")
                                              .setOptions(List.of("You", "Me", "Them", "It"))
                                              .setPoint(2)
                    );
                }})
                .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0));
        
        examTakenRepository.saveAll(List.of(
                ExamTakenMapper.toExamTaken(examTakenRequest1)
                               .setUser(userRepository.findUserByEmail("aalex@cbt.com").get())
                               .setTotalPoints(75.0)
                               .setPassed(true)
                               .setExam(examN103),
                ExamTakenMapper.toExamTaken(examTakenRequest1)
                        .setUser(userRepository.findUserByEmail("bobreed@cbt.com").get())
                        .setTotalPoints(75.0)
                        .setPassed(true)
                        .setExam(examN104),
                ExamTakenMapper.toExamTaken(examTakenRequest2)
                        .setUser(userRepository.findUserByEmail("aalex@cbt.com").get())
                        .setTotalPoints(75.0)
                        .setPassed(true)
                        .setExam(examN104)
        ));
        
        List<ExamTaken> examsByUser = examTakenRepository.findAllByExam(examN103);
        assertThat(examsByUser.size()).isEqualTo(1);
    
        Exam nonExistentExam = examRepository.save(
                ExamMapper.toExam(getExam("N101",List.of("aalex@cbt.com","bobreed@cbt.com"))));
        List<ExamTaken> examsByNonExistentExam = examTakenRepository.findAllByExam(nonExistentExam);
        assertThat(examsByNonExistentExam.size()).isEqualTo(0);
    }
    @Test
    public void findByUserIdAndExamIdTest(){
        Exam examN103 = ExamMapper.toExam(getExam("N101",List.of("aalex@cbt.com","bobreed@cbt.com")));
        examRepository.save(examN103);
        
        User alice = userRepository.findUserByEmail("aalex@cbt.com")
                .orElseThrow(()->new ResourceNotFoundException("No such user"));
        
        examTakenRepository.save(
                new ExamTaken()
                        .setExam(examN103)
                        .setResponses(new ArrayList<>(){{
                            add(new QuestionResponse().setUserChoice("Me")
                                                      .setText("How are you?")
                                                      .setAnswer("Me")
                                                      .setOptions(List.of("Hello", "Me", "You", "Him"))
                                                      .setPoint(5)
                            );
                            add(new QuestionResponse().setUserChoice("Him")
                                                      .setText("Who is she?")
                                                      .setAnswer("Her")
                                                      .setOptions(List.of("Her", "Him", "They", "Them"))
                                                      .setPoint(3)
                            );
                            add(new QuestionResponse().setUserChoice("it")
                                                      .setText("Who is he?")
                                                      .setAnswer("It")
                                                      .setOptions(List.of("You", "Me", "Them", "It"))
                                                      .setPoint(2)
                            );
                        }})
                        .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0))
        );
    
        ExamTakenRequest examTakenRequest = new ExamTakenRequest()
                .setExamId(examN103.getId())
                .setResponses(new ArrayList<>(){{
                    add(new QuestionResponse().setUserChoice("Me")
                                              .setText("How are you?")
                                              .setAnswer("Me")
                                              .setOptions(List.of("Hello", "Me", "You", "Him"))
                                              .setPoint(5)
                    );
                    add(new QuestionResponse().setUserChoice("Him")
                                              .setText("Who is she?")
                                              .setAnswer("Her")
                                              .setOptions(List.of("Her", "Him", "They", "Them"))
                                              .setPoint(3)
                    );
                    add(new QuestionResponse().setUserChoice("it")
                                              .setText("Who is he?")
                                              .setAnswer("It")
                                              .setOptions(List.of("You", "Me", "Them", "It"))
                                              .setPoint(2)
                    );
                }})
                .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0));
    
        examTakenRepository.saveAll(List.of(
                ExamTakenMapper.toExamTaken(examTakenRequest)
                               .setUser(alice)
                               .setTotalPoints(75.0)
                               .setPassed(true)
                               .setExam(examN103)
        ));
    
    
        Optional<ExamTaken> examsByUser = examTakenRepository.findOneByUserIdAndExamId(alice, examN103);
        assertThat(examsByUser).isPresent();
    
        Exam nonExistentExam = examRepository.save(
                ExamMapper.toExam(getExam("N101",List.of("aalex@cbt.com","bobreed@cbt.com")))
        );
        Optional<ExamTaken> examsByNonExistentExam = examTakenRepository.findOneByUserIdAndExamId(alice, nonExistentExam);
        assertThat(examsByNonExistentExam).isEmpty();
    }
    
    private ExamRequest getExam(String examNumber, List<String> candidateEmails){
        return new ExamRequest()
                .setExamNumber(examNumber)
                .setName("Nexam")
                .setPassMark(3)
                .setDescription("Quisque porta volutpat erat. Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla. Nunc purus.")
                .setInstructions("Duis consequat dui nec nisi volutpat eleifend. Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus. Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis. Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci.")
                .setStart(LocalDateTime.of(1992, 12, 12, 12, 0))
                .setDuration(5000)
                .setTimed(true)
                .setQuestions(getExamQuestions())
                .setCandidates(new HashSet<>(){
                    {
                        addAll(candidateEmails);
                        //add("aalex@cbt.com");
                        //add("bobreed@cbt.com");
                    }
                });
    }
    private List<Question> getExamQuestions(){
        return List.of(
                new Question()
                        .setText("How are you?")
                        .setPoint(5)
                        .setOptions(List.of("Hello", "Me", "You", "Him"))
                        .setAnswer("Me"),
                new Question()
                        .setText("Who is she?")
                        .setPoint(3)
                        .setOptions(List.of("Her", "Him", "They", "Them"))
                        .setAnswer("Her"),
                new Question()
                        .setText("Who is he?")
                        .setPoint(2)
                        .setOptions(List.of("You", "Me", "Them", "It"))
                        .setAnswer("It")
        );
    }
}
