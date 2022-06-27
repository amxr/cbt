package com.fip.cbt.repository;

import com.fip.cbt.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExamTakenRepositoryTest {
    @Autowired
    private TakenExamRepository takenExamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

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
        takenExamRepository.deleteAll();
    }

    @AfterAll
    void afterAll(){
        examRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void findAllByCandidateTest(){
        takeExam(N101, alice);
        takeExam(N101, bob);
        takeExam(N101, alice);

        //List<ExamTaken> t = examTakenRepository.findAll();
        List<TakenExam> examsTakenByAlice = takenExamRepository.findAllByCandidate(alice);
        assertThat(examsTakenByAlice.size()).isEqualTo(2);

        List<TakenExam> examsTakenByBob = takenExamRepository.findAllByCandidate(bob);
        assertThat(examsTakenByBob.size()).isEqualTo(1);
    }

    @Test
    public void findByCandidateAndExamTest(){
        takeExam(N101, alice);
        takeExam(N101, bob);

        Optional<TakenExam> takenN101Exams = takenExamRepository.findByCandidateAndExam(alice, N101);
        assertThat(takenN101Exams.isPresent()).isTrue();

        Optional<TakenExam> takenN102Exams = takenExamRepository.findByCandidateAndExam(alice, N102);
        assertThat(takenN102Exams.isPresent()).isFalse();
    }

    @Test
    public void findAllByExam_OwnerTest(){
        List<TakenExam> takenExams = takenExamRepository.findAllByExam_Owner(flexisaf);
        assertThat(takenExams.isEmpty()).isTrue();

        takeExam(N101, alice);
        takeExam(N101, bob);

        takenExams = takenExamRepository.findAllByExam_Owner(flexisaf);
        assertThat(takenExams.size()).isEqualTo(2);
    }

    @Test
    public void existsByCandidateAndExamTest(){
        boolean check = takenExamRepository.existsByCandidateAndExam(alice, N101);
        assertThat(check).isFalse();

        takeExam(N101, alice);

        check = takenExamRepository.existsByCandidateAndExam(alice, N101);
        assertThat(check).isTrue();
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
        TakenExam takenExam = new TakenExam()
                .setExam(exam)
                .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0))
                .setCandidate(user);

        takenExamRepository.save(takenExam);
    }
}
