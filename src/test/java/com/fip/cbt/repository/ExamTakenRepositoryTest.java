package com.fip.cbt.repository;

import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.model.*;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExamTakenRepositoryTest {
    @Autowired
    ExamTakenRepository examTakenRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ExamRepository examRepository;

    Exam N101, N102;
    User alice, bob, flexisaf;

    @BeforeAll
    void beforeAll(){
        userRepository.deleteAll();
        Map<String, User> users = createAndReturnCandidates();
        alice = users.get("aalex@cbt.com");
        bob = users.get("bobreed@cbt.com");
        flexisaf = createAndReturnTestOwner();

        Map<String, Exam> exams = createAndReturnExams();

        N101 = exams.get("N101");
        N102 = exams.get("N102");
    }

    @AfterEach
    void tearDown(){
        examTakenRepository.deleteAll();
    }

    @AfterAll
    void afterAll(){
        userRepository.deleteAll();
        examRepository.deleteAll();
    }
    
    @Test
    public void findAllByUserTest(){
        takeExam(N101, alice);
        takeExam(N101, bob);
        takeExam(N101, alice);

        //List<ExamTaken> t = examTakenRepository.findAll();
        List<ExamTaken> examsTakenByAlice = examTakenRepository.findAllByUser(alice);
        assertThat(examsTakenByAlice.size()).isEqualTo(2);

        List<ExamTaken> examsTakenByBob = examTakenRepository.findAllByUser(bob);
        assertThat(examsTakenByBob.size()).isEqualTo(1);
    }

    @Test
    public void findAllByExamTest(){
        takeExam(N101, alice);
        takeExam(N101, bob);

        List<ExamTaken> takenN101Exams = examTakenRepository.findAllByExam(N101);
        assertThat(takenN101Exams.size()).isEqualTo(2);

        List<ExamTaken> takenN102Exams = examTakenRepository.findAllByExam(N102);
        assertThat(takenN102Exams.size()).isEqualTo(0);
    }

    @Test
    public void findByUserAndExamTest(){
        takeExam(N101, alice);

        Optional<ExamTaken> examsByAlice = examTakenRepository.findOneByUserAndExam(alice, N101);
        assertThat(examsByAlice).isPresent();

        Optional<ExamTaken> examsByBob = examTakenRepository.findOneByUserAndExam(bob, N101);
        assertThat(examsByBob).isEmpty();

    }

//    @Test
//    public void findAllByExamOwnerTest(){
//        takeExam(N101, alice);
//        takeExam(N102, bob);
//
//        List<ExamTaken> examsByFlexisaf = examTakenRepository.findAllByExamOwnerId(flexisaf.getId());
//        assertThat(examsByFlexisaf.size()).isEqualTo(2);
//
//        List<ExamTaken> examsByAlice = examTakenRepository.findAllByExamOwnerId(alice.getId());
//        assertThat(examsByAlice.size()).isEqualTo(0);
//    }

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

    private Exam getExam(String examNumber){
        return new Exam()
                .setExamNumber(examNumber)
                .setOwner(flexisaf)
                .setName("Nexam")
                .setPassMark(3)
                .setDescription("Quisque porta volutpat erat. Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla. Nunc purus.")
                .setInstructions("Duis consequat dui nec nisi volutpat eleifend. Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus. Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis. Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci.")
                .setStart(LocalDateTime.of(1992, 12, 12, 12, 0))
                .setDuration(5000)
                .setTimed(true)
                .setQuestions(getExamQuestions())
                .setOpen(true);
    }

    private User createAndReturnTestOwner(){
        User flexisaf = new User()
                .setEmail("admin@flexisaf.com")
                .setPassword("administrator")
                .setName("flexisaf")
                .setRole(Role.TESTOWNER)
                .setEnabled(true);
        return userRepository.save(flexisaf);
    }

    private Map<String, User> createAndReturnCandidates(){
        User alice = new User()
                .setName("Alice Alex")
                .setEmail("aalex@cbt.com")
                .setPassword("aliceAlex123")
                .setRole(Role.CANDIDATE)
                .setEnabled(true);
        User bob = new User()
                .setName("Robert Reed")
                .setEmail("bobreed@cbt.com")
                .setPassword("bobbyreeder12")
                .setRole(Role.CANDIDATE)
                .setEnabled(true);

        List<User> users = userRepository.saveAll(List.of(alice, bob));

        return Map.of(users.get(0).getUsername(), users.get(0), users.get(1).getUsername(), users.get(1));
    }

    private Map<String, Exam> createAndReturnExams(){
        Exam examN101 = getExam("N101")
                .setCandidates(Set.of(alice, bob));

        Exam examN102 = getExam("N102")
                .setCandidates(Set.of(alice));
        List<Exam> exams = examRepository.saveAll(List.of(examN101, examN102));

        return Map.of(exams.get(0).getExamNumber(), exams.get(0), exams.get(1).getExamNumber(), exams.get(1));
    }

    private void takeExam(Exam exam, User user){
        ExamTaken takenExam = new ExamTaken()
                .setExam(exam)
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
                .setUser(user);

        examTakenRepository.save(takenExam);
    }
}
