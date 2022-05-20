package com.fip.cbt.service;

import com.fip.cbt.controller.request.AddCandidatesRequest;
import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.controller.request.UpdateExamRequest;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.mapper.ExamMapper;
import com.fip.cbt.model.Exam;
import com.fip.cbt.model.Question;
import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.ExamRepository;
import com.fip.cbt.repository.UserRepository;
import com.fip.cbt.service.impl.ExamServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {
    
    @Mock
    ExamRepository examRepository;
    
    @Mock
    UserRepository userRepository;
    
    @Autowired
    @InjectMocks
    ExamServiceImpl examService;
    
    @AfterEach
    void tearDown(){
        examRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    @Test
    public void addExamTest(){
        ExamRequest examRequest = getExam("N102",List.of("aalex@cbt.com","bobreed@cbt.com"));
        when(examRepository.save(any(Exam.class))).thenReturn(ExamMapper.toExam(examRequest));
        User alice = new User().setName("Alice Alex")
                               .setEmail("aalex@cbt.com")
                               .setPassword("aliceAlex123")
                               .setRole(Role.CANDIDATE);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(alice));
        
        Exam exam = examService.add(examRequest);
        
        assertEquals(exam.getExamNumber(), "N102");
    }
    
    @Test
    public void getOneTest(){
        String id = "examId";
    
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            examService.delete(id);
        });
    
        assertEquals(e.getMessage(), "Exam with number " + id + " not found.");
    
        when(examRepository.findExamByExamNumber(any(String.class))).thenReturn(Optional.of(new Exam()));
    
        Exam exam = examService.getOne(id);
    
        assertThat(exam).isNotNull();
    }
    
    @Test
    public void delete(){
        String id = "examId";
    
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            examService.delete(id);
        });
    
        assertEquals(e.getMessage(), "Exam with number " + id + " not found.");
    
        when(examRepository.findExamByExamNumber(any(String.class))).thenReturn(Optional.of(new Exam()));
    
        examService.delete(id);
    
        verify(examRepository, times(1)).delete(any(Exam.class));
    }
    
    @Test
    public void updateTest(){
        String id = "examId";
    
        when(examRepository.findById(any(String.class))).thenReturn(Optional.of(new Exam()));
        
        UpdateExamRequest updateExamRequest = new UpdateExamRequest()
                .setId(id);
        when(examRepository.save(any(Exam.class))).thenReturn(ExamMapper.toExam(updateExamRequest));
        
        Exam exam = examService.update(updateExamRequest);
        assertEquals(exam.getId(), id);
    }
    
    @Test
    public void getAllTest(){
        when(examRepository.findAll()).thenReturn(List.of(new Exam(),
                                                          new Exam(),
                                                          new Exam())
        );
        List<Exam> exams = examService.getAll();
        assertThat(exams.size()).isEqualTo(3);
    }
    
    @Test
    public void addCandidatesTest(){
        Exam mockedExam = new Exam().setCandidates(new HashSet<>(){
            {
                add(new User());
                add(new User());
            }
        });
        when(examRepository.findExamByExamNumber(any(String.class))).thenReturn(Optional.of(mockedExam));
    
        AddCandidatesRequest newCandidatesRequest = new AddCandidatesRequest()
                .setCandidates(new HashSet<>(){
                    {
                        add("aalex@cbt.com");
                        add("bobreed@cbt.com");
                    }
                });
        
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(new User()));
        when(examRepository.save(any(Exam.class))).thenReturn(new Exam());
        
        Exam addedCandidatesExam = examService.addCandidates("examNumber", newCandidatesRequest);
        assertThat(addedCandidatesExam).isNotNull();
    }
    
    @Test
    public void registerUserTest(){
        Exam mockedExam = new Exam().setCandidates(new HashSet<>(){
            {
                add(new User());
                add(new User());
            }
        });
        User applyingUser = new User()
                .setEmail("johndoe@cbt.com")
                        .setPassword("johnnydoe");
        
        when(examRepository.findExamByExamNumber(any(String.class))).thenReturn(Optional.of(mockedExam));
        
        when(examRepository.save(any(Exam.class))).thenReturn(new Exam());
        
        Exam addedCandidatesExam = examService.registerUser("examNumber", applyingUser);
        assertThat(addedCandidatesExam).isNotNull();
    }
    
    private ExamRequest getExam(String id, List<String> candidateEmails){
        return new ExamRequest()
                .setExamNumber(id)
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
