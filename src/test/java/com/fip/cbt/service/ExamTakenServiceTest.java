//package com.fip.cbt.service;
//
//import com.fip.cbt.controller.request.ExamRequest;
//import com.fip.cbt.controller.request.TakenExamRequest;
//import com.fip.cbt.exception.ResourceNotFoundException;
//import com.fip.cbt.mapper.ExamMapper;
//import com.fip.cbt.mapper.ExamTakenMapper;
//import com.fip.cbt.model.*;
//import com.fip.cbt.repository.ExamRepository;
//import com.fip.cbt.repository.ExamTakenRepository;
//import com.fip.cbt.repository.UserRepository;
//import com.fip.cbt.service.impl.ExamTakenServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ExamTakenServiceTest {
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    ExamTakenRepository examTakenRepository;
//
//    @Mock
//    ExamRepository examRepository;
//
//    @Autowired
//    @InjectMocks
//    ExamTakenServiceImpl examTakenService;
//
//    @Test
//    public void addTest(){
//        ExamRequest examRequest = getExam("N101",List.of("aalex@cbt.com"));
//        Exam examN101 = ExamMapper.toExam(examRequest);
//        examN101.setCandidates(examRequest.getCandidates());
//        when(examRepository.findById(any(String.class))).thenReturn(Optional.of(examN101));
//
//        TakenExamRequest newExamTakenRequest = getExamTaken(examN101.getExamNumber());
//
//        User bob = new User()
//                .setEmail("bobreed@cbt.com")
//                .setPassword("bobbyreeder12")
//                .setRole(Role.CANDIDATE);
//        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
//
//        when(examTakenRepository.findOneByUserIdAndExamId(any(User.class), any(Exam.class))).thenReturn(Optional.empty());
//
//        ExamTaken mappedExamTaken = ExamTakenMapper.toExamTaken(newExamTakenRequest)
//                                                           .setExam(examN101)
//                                                                   .setUser(bob);
//        when(examTakenRepository.save(any(ExamTaken.class))).thenReturn(mappedExamTaken);
//        ExamTaken submittedExam = examTakenService.add(newExamTakenRequest, bob);
//        assertThat(submittedExam.getExam().getExamNumber()).isEqualTo(examN101.getExamNumber());
//        assertThat(submittedExam.getUser().getUsername()).isEqualTo(bob.getUsername());
//    }
//
//    @Test
//    public void getOneTest(){
//        String id = "examId";
//
//        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
//            examTakenService.delete(id);
//        });
//
//        assertEquals(e.getMessage(), "Could not find Exam with id " +id);
//
//        when(examTakenRepository.findById(any(String.class))).thenReturn(Optional.of(new ExamTaken()));
//
//        ExamTaken examTaken = examTakenService.getOne(id);
//
//        assertThat(examTaken).isNotNull();
//    }
//
//    @Test
//    public void getAll(){
//
//        TakenExamRequest examTakenRequest1 = new TakenExamRequest()
//                .setExamId("examN103Id()")
//                .setResponses(new ArrayList<>(){{
//                    add(new QuestionResponse());
//                    add(new QuestionResponse());
//                    add(new QuestionResponse());
//                }});
//
//        TakenExamRequest examTakenRequest2 = new TakenExamRequest()
//                .setExamId("examN104Id")
//                .setResponses(new ArrayList<>(){{
//                    add(new QuestionResponse());
//                    add(new QuestionResponse());
//                    add(new QuestionResponse());
//                }});
//
//
//       User bob = new User()
//                .setName("Robert Reed")
//                .setEmail("bobreed@cbt.com")
//                .setPassword("bobbyreeder12")
//               .setRole(Role.CANDIDATE);
//       when(examTakenRepository.findAllByUserId(any(User.class)))
//                .thenReturn(List.of(ExamTakenMapper.toExamTaken(examTakenRequest1),
//                                    ExamTakenMapper.toExamTaken(examTakenRequest2))
//       );
//
//       when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
//
//        //First case of user requests their taken exams
//       List<ExamTaken> userExams = examTakenService.getAll("", "", bob);
//       assertThat(userExams.size()).isEqualTo(2);
//    }
//
//    @Test
//    public void getAllByUserAndByExamTest(){
//        TakenExamRequest examTakenRequest1 = new TakenExamRequest()
//                .setExamId("examN103Id()")
//                .setResponses(new ArrayList<>(){{
//                    add(new QuestionResponse());
//                    add(new QuestionResponse());
//                    add(new QuestionResponse());
//                }});
//
//        TakenExamRequest examTakenRequest2 = new TakenExamRequest()
//                .setExamId("examN104Id")
//                .setResponses(new ArrayList<>(){{
//                    add(new QuestionResponse());
//                    add(new QuestionResponse());
//                    add(new QuestionResponse());
//                }});
//
//        //Second case of admin requesting for exams taken by specific user
//        User admin = new User()
//                .setName("ADMIN")
//                .setEmail("admin@cbt.com")
//                .setPassword("admin12")
//                .setRole(Role.ADMINISTRATOR);
//
//        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(admin));
//        when(examRepository.findExamByExamNumber(any(String.class))).thenReturn(Optional.of(new Exam()));
//        when(examTakenRepository.findAllByUserId(any(User.class)))
//                .thenReturn(List.of(ExamTakenMapper.toExamTaken(examTakenRequest1),
//                                    ExamTakenMapper.toExamTaken(examTakenRequest2))
//        );
//
//        List<ExamTaken> examsByUser = examTakenService.getAll("bob", "", admin);
//        assertThat(examsByUser.size()).isEqualTo(2);
//
//        //Third case of admin requesting for specific exams
//        when(examTakenRepository.findAllByExam(any(Exam.class)))
//                .thenReturn(List.of(ExamTakenMapper.toExamTaken(examTakenRequest1),
//                                    ExamTakenMapper.toExamTaken(examTakenRequest2))
//        );
//        List<ExamTaken> exams = examTakenService.getAll("", "examNumber", admin);
//        assertThat(exams.size()).isEqualTo(2);
//    }
//
//    @Test
//    public void delete(){
//        String id = "examId";
//
//        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
//            examTakenService.delete(id);
//        });
//
//        assertEquals(e.getMessage(), "Could not find Exam with id " +id);
//
//        when(examTakenRepository.findById(any(String.class))).thenReturn(Optional.of(new ExamTaken()));
//
//        examTakenService.delete(id);
//
//        verify(examTakenRepository, times(1)).delete(any(ExamTaken.class));
//    }
//
//    private ExamRequest getExam(String id, List<String> candidateEmails){
//        return new ExamRequest()
//                .setExamNumber(id)
//                .setName("Nexam")
//                .setPassMark(3)
//                .setDescription("Quisque porta volutpat erat. Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla. Nunc purus.")
//                .setInstructions("Duis consequat dui nec nisi volutpat eleifend. Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus. Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis. Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci.")
//                .setStart(LocalDateTime.of(1992, 12, 12, 12, 0))
//                .setDuration(5000)
//                .setTimed(true)
//                .setQuestions(getExamQuestions())
//                .setCandidates(new HashSet<>(){
//                    {
//                        //addAll(candidateEmails);
//                        add(new User().setEmail("aalex@cbt.com").setPassword("aliceAlex123").setRole(Role.CANDIDATE));
//                        add(new User().setEmail("bobreed@cbt.com").setPassword("bobbyreeder12").setRole(Role.CANDIDATE));
//                    }
//                });
//    }
//    private List<Question> getExamQuestions(){
//        return List.of(
//                new Question()
//                        .setText("How are you?")
//                        .setPoint(5)
//                        .setOptions(List.of("Hello", "Me", "You", "Him"))
//                        .setAnswer("Me"),
//                new Question()
//                        .setText("Who is she?")
//                        .setPoint(3)
//                        .setOptions(List.of("Her", "Him", "They", "Them"))
//                        .setAnswer("Her"),
//                new Question()
//                        .setText("Who is he?")
//                        .setPoint(2)
//                        .setOptions(List.of("You", "Me", "Them", "It"))
//                        .setAnswer("It")
//        );
//    }
//    private TakenExamRequest getExamTaken(String id){
//        return new TakenExamRequest()
//                .setExamId(id)
//                .setResponses(new ArrayList<>(){{
//                    add(new QuestionResponse().setUserChoice("Me")
//                                              .setText("How are you?")
//                                              .setAnswer("Me")
//                                              .setOptions(List.of("Hello", "Me", "You", "Him"))
//                                              .setPoint(5)
//                    );
//                    add(new QuestionResponse().setUserChoice("Him")
//                                              .setText("Who is she?")
//                                              .setAnswer("Her")
//                                              .setOptions(List.of("Her", "Him", "They", "Them"))
//                                              .setPoint(3)
//                    );
//                    add(new QuestionResponse().setUserChoice("it")
//                                              .setText("Who is he?")
//                                              .setAnswer("It")
//                                              .setOptions(List.of("You", "Me", "Them", "It"))
//                                              .setPoint(2)
//                    );
//                }})
//                .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0));
//    }
//}