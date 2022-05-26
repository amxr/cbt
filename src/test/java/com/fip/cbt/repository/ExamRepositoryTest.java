//package com.fip.cbt.repository;
//
//import com.fip.cbt.controller.request.ExamRequest;
//import com.fip.cbt.dto.mapper.ExamMapper;
//import com.fip.cbt.model.Exam;
//import com.fip.cbt.model.Question;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataMongoTest
//@ExtendWith(MockitoExtension.class)
//public class ExamRepositoryTest {
//    @Autowired
//    ExamRepository examRepository;
//
//    @AfterEach
//    void tearDown(){
//        examRepository.deleteAll();
//    }
//
//    @BeforeEach
//    void setUp(){
//        /*Exam newExam1 =*/ examRepository.save(
//                ExamMapper.toExam(
//                        getExam("N101", "Nexam", LocalDateTime.of(1992, 12, 12, 12, 0),
//                                50000)
//                )
//        );
//        /*Exam newExam2 =*/ examRepository.save(
//                ExamMapper.toExam(
//                        getExam("N102","Nexam", LocalDateTime.of(1992, 12, 12, 12, 0),
//                                40000)
//                )
//        );
//        /*Exam newExam3 =*/ examRepository.save(
//                ExamMapper.toExam(
//                        getExam("N103", "Pexam", LocalDateTime.of(1942, 12, 12, 12, 0),
//                                40000)
//                )
//        );
//    }
//
//    @Test
//    public void createAndDeleteExams(){
//        //when
//        List<Exam> exams = examRepository.findAll();
//
//        //then
//        assertThat(exams.size()).isEqualTo(3);
//
//        //when
//        examRepository.deleteAll();
//        List<Exam> examsAfterDeletion = examRepository.findAll();
//
//        //then
//        assertThat(examsAfterDeletion.size()).isEqualTo(0);
//
//    }
//    @Test
//    public void findByExamNumberTest(){
//        /*Exam newExam1 = examRepository.save(ExamMapper.toExam(getExam("N101")));
//        Exam newExam2 = examRepository.save(ExamMapper.toExam(getExam("N102")));
//        Exam newExam3 = examRepository.save(ExamMapper.toExam(getExam("N103")));
//        */
//        Optional<Exam> findExam = examRepository.findExamByExamNumber("N102");
//        assertThat(findExam).isPresent();
//        assertThat(findExam.get().getExamNumber()).isEqualTo("N102");
//
//        Optional<Exam> findNonExistentExam = examRepository.findExamByExamNumber("N201");
//        assertThat(findNonExistentExam).isEmpty();
//    }
//    @Test
//    public void findExamByNameTest(){
//
//        List<Exam> findExams = examRepository.findExamByName("Nexam");
//        assertThat(findExams.size()).isEqualTo(2);
//        examRepository.delete(findExams.get(0));
//
//        List<Exam> findRemainingExams = examRepository.findExamByName("Nexam");
//        assertThat(findRemainingExams.size()).isEqualTo(1);
//    }
//    @Test
//    public void findExamByDateTest(){
//
//        List<Exam> findExams = examRepository.findExamByDate(LocalDateTime.of(1992, 12, 12, 12, 0));
//        assertThat(findExams.size()).isEqualTo(2);
//
//        List<Exam> findNonExistentExamByHour = examRepository.findExamByDate(LocalDateTime.of(1993, 12, 12, 13, 0));
//        assertThat(findNonExistentExamByHour.size()).isEqualTo(0);
//        List<Exam> findNonExistentExamByDay = examRepository.findExamByDate(LocalDateTime.of(1993, 12, 2, 12, 1));
//        assertThat(findNonExistentExamByDay.size()).isEqualTo(0);
//    }
//     @Test
//    public void findExamByDurationTest(){
//
//        List<Exam> findExams = examRepository.findExamByDuration(40000);
//        assertThat(findExams.size()).isEqualTo(2);
//
//        List<Exam> findNonExistentExamByDuration = examRepository.findExamByDuration(5000);
//        assertThat(findNonExistentExamByDuration.size()).isEqualTo(0);
//    }
//
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
//
//    private ExamRequest getExam(String examNumber, String examName,LocalDateTime dateTime, int duration){
//        return new ExamRequest()
//                .setExamNumber(examNumber)
//                .setName(examName)
//                .setPassMark(3)
//                .setDescription("Quisque porta volutpat erat. Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla. Nunc purus.")
//                .setInstructions("Duis consequat dui nec nisi volutpat eleifend. Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus. Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis. Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci.")
//                .setStart(dateTime)
//                .setDuration(duration)
//                .setTimed(true)
//                .setQuestions(getExamQuestions())
//                .setCandidates(new HashSet<>(){
//                    {
//                        add("aalex@cbt.com");
//                        add("bobreed@cbt.com");
//                    }
//                });
//    }
//}
