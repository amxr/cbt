package com.fip.cbt.repository;

import com.fip.cbt.model.Exam;
import com.fip.cbt.model.Question;
import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExamRepositoryTest {
    @Autowired
    ExamRepository examRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    List<Question> questions;

    Map<String, User> users;

    @BeforeAll
    void beforeAll(){
        users = createAndGetUsers();
    }

    @BeforeEach
    void setUp(){
        createExam(
                users.get("testowner1"),
                "N101",
                Set.of(users.get("alice"), users.get("bob")),
                Set.of(users.get("charlie"), users.get("amir")));

        createExam(
                users.get("testowner2"),
                "N102",
                Set.of(users.get("amir"), users.get("bob")),
                Set.of(users.get("charlie"), users.get("alice")));
    }

    @AfterEach
    void tearDown(){
        examRepository.deleteAll();
    }

    @Test
    public void createAndDeleteExams(){
        createExam(
                users.get("testowner1"),
                "N103",
                Set.of(users.get("charlie"), users.get("amir")),
                Set.of(users.get("bob"), users.get("alice")));

        //when
        List<Exam> exams = examRepository.findAll();

        //then
        assertThat(exams.size()).isEqualTo(3);

        //when
        questionRepository.deleteAll();
        examRepository.deleteAll();
        List<Exam> examsAfterDeletion = examRepository.findAll();

        //then
        assertThat(examsAfterDeletion.size()).isEqualTo(0);

    }
    @Test
    public void findByExamNumberTest(){

        Optional<Exam> findExam = examRepository.findExamByExamNumber("N102");
        assertThat(findExam).isPresent();
        assertThat(findExam.get().getExamNumber()).isEqualTo("N102");

        Optional<Exam> findNonExistentExam = examRepository.findExamByExamNumber("N201");
        assertThat(findNonExistentExam).isEmpty();
    }

     @Test
    public void findExamByOwnerTest(){
        List<Exam> findExams = examRepository.findAllByOwner(users.get("testowner1"));
        assertThat(findExams.size()).isEqualTo(1);

        User newTestOwner = new User().setName("testowner3")
                .setEmail("testowner3@cbt.com")
                .setPassword("testowner3")
                .setRole(Role.TESTOWNER);

        User testowner3 = userRepository.save(newTestOwner);

        List<Exam> findExamWithNonExistentOwner = examRepository.findAllByOwner(testowner3);
        assertThat(findExamWithNonExistentOwner.size()).isEqualTo(0);
    }


    private void createAndGetQuestions(Exam exam){
        List<Question> questions = List.of(
                new Question()
                        .setExam(exam)
                        .setText("How are you?")
                        .setPoint(5)
                        .setOptions(List.of("Hello", "Me", "You", "Him"))
                        .setAnswer("Me"),
                new Question()
                        .setExam(exam)
                        .setText("Who is she?")
                        .setPoint(3)
                        .setOptions(List.of("Her", "Him", "They", "Them"))
                        .setAnswer("Her"),
                new Question()
                        .setExam(exam)
                        .setText("Who is he?")
                        .setPoint(2)
                        .setOptions(List.of("You", "Me", "Them", "It"))
                        .setAnswer("It")
        );

        questionRepository.saveAll(questions);
    }

    private Map<String, User> createAndGetUsers(){
        User testowner1 = new User().setName("testowner1")
                .setEmail("testowner1@cbt.com")
                .setPassword("testowner1")
                .setRole(Role.TESTOWNER);
        User testowner2 = new User().setName("testowner2")
                .setEmail("testowner2@cbt.com")
                .setPassword("testowner2")
                .setRole(Role.TESTOWNER);

        User alice = new User().setName("alice")
                .setEmail("aalex@cbt.com")
                .setPassword("aliceAlex123")
                .setRole(Role.CANDIDATE);
        User bob = new User().setName("bob")
                .setEmail("bobreed@cbt.com")
                .setPassword("bobbyreeder1")
                .setRole(Role.CANDIDATE);
        User charlie = new User().setName("charlie")
                .setEmail("ccousy@cbt.com")
                .setPassword("charliecousy")
                .setRole(Role.CANDIDATE);
        User amir = new User().setName("amir")
                .setEmail("amir@gmail.com")
                .setPassword("amiramir")
                .setRole(Role.CANDIDATE);

        List<User> users = userRepository.saveAll(List.of(testowner1, testowner2, alice, bob,charlie, amir));

        Map<String, User> usersMap = new HashMap<>();
        for(User user: users){
            usersMap.put(user.getName(), user);
        }

        return usersMap;
    }
    private void createExam(User owner, String examNumber, Set<User> candidates, Set<User> registeredCandidates){
        Exam exam = new Exam()
                .setOwner(owner)
                .setExamNumber(examNumber)
                .setName("Nexam")
                .setPassMark(3)
                .setDescription("Quisque porta volutpat erat. Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla. Nunc purus.")
                .setInstructions("Duis consequat dui nec nisi volutpat eleifend. Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus. Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis. Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci.")
                .setStart(LocalDateTime.of(2023, 12, 12, 12, 0))
                .setCandidates(candidates)
                .setRegisteredCandidates(registeredCandidates)
                .setDuration(5000)
                .setTimed(true)
                .setOpen(true);

        Exam savedExam = examRepository.save(exam);

        createAndGetQuestions(savedExam);
    }


}