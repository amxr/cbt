package com.fip.cbt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fip.cbt.CbtApplication;
import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.model.Exam;
import com.fip.cbt.model.Question;
import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.ExamRepository;
import com.fip.cbt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CbtApplication.class})
class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private UserRepository userRepository;

    private final String URI = "/api/v1/exam";

    @BeforeEach
    void setUp(){
        User alice = new User().setName("Alice Alex")
                               .setEmail("aalex@cbt.com")
                               .setPassword("aliceAlex123")
                               .setRole(Role.CANDIDATE);
        User bob = new User().setName("Robert Reed")
                             .setEmail("bobreed@cbt.com")
                             .setPassword("bobbyreeder12")
                             .setRole(Role.CANDIDATE);
        userRepository.saveAll(List.of(alice, bob));
    }
    
    @AfterEach
    void tearDown() {
        examRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ADMINISTRATOR"})
    void addExamTest() throws Exception {
        ExamRequest newExam = getExam();
        MvcResult result = mockMvc.perform(
                post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(newExam))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value(newExam.getName()))
                .andExpect(jsonPath("$.timed").value(true))
                .andReturn();

        mockMvc.perform(
                        post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(newExam))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_message").value("Exam with number " + newExam.getExamNumber() + " already exists."))
                .andExpect(jsonPath("$.error_code").value("CONFLICT"));

        Exam exam = mapFromJson(result.getResponse().getContentAsString(), Exam.class);
        assertNotNull(exam);
        assertEquals(exam.getQuestions().size(), newExam.getQuestions().size());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ADMINISTRATOR"})
    void getAllExamsTest() throws Exception {
        ExamRequest newExam = getExam();
        mockMvc.perform(
                post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(newExam))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value(newExam.getName()))
                .andExpect(jsonPath("$.exam_number").value("N101"))
                .andExpect(jsonPath("$.timed").value(true));

        newExam.setExamNumber("NS101");
        mockMvc.perform(
                post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(newExam))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.exam_number").value("NS101"))
                .andExpect(jsonPath("$.timed").value(true));

        MvcResult result = mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Exam[] exams = mapFromJson(content, Exam[].class);
        assertEquals(2, exams.length);
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ADMINISTRATOR"})
    void getOneExamTest() throws Exception{
        ExamRequest newExam = getExam();
        mockMvc.perform(get(URI+"/"+newExam.getExamNumber()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_message").value("Exam with number " + newExam.getExamNumber() + " not found."))
                .andExpect(jsonPath("$.error_code").value("NOT_FOUND"));


        mockMvc.perform(
                        post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(newExam))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value(newExam.getName()))
                .andExpect(jsonPath("$.exam_number").value("N101"))
                .andExpect(jsonPath("$.timed").value(true));

        MvcResult result = mockMvc
                .perform(get(URI+"/"+newExam.getExamNumber()))
                .andExpect(status().isFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Exam exam = mapFromJson(content, Exam.class);

        assertEquals(exam.getExamNumber(), newExam.getExamNumber());
        assertEquals(exam.getQuestions().size(), newExam.getQuestions().size());
        assertEquals(exam.getName(), newExam.getName());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ADMINISTRATOR"})
    void deleteExamTest() throws Exception{
        //TODO: Review this test
        ExamRequest newExam = getExam();
        mockMvc.perform(delete(URI+"/"+newExam.getExamNumber()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_message").value("Exam with number " + newExam.getExamNumber() + " not found."))
                .andExpect(jsonPath("$.error_code").value("NOT_FOUND"));


        mockMvc.perform(
                        post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(newExam))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value(newExam.getName()))
                .andExpect(jsonPath("$.exam_number").value("N101"))
                .andExpect(jsonPath("$.timed").value(true));

        mockMvc
                .perform(delete(URI+"/"+newExam.getExamNumber()))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Exam[] exams = mapFromJson(content, Exam[].class);
        assertEquals(0, exams.length);
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ADMINISTRATOR"})
    void updateExamTest() throws Exception{
        ExamRequest newExam = getExam();
        MvcResult result = mockMvc.perform(
                        post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(newExam))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value(newExam.getName()))
                .andExpect(jsonPath("$.timed").value(true))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Exam exam = mapFromJson(content, Exam.class);

        Question newQ = new Question()
                .setText("Who is the president?")
                .setPoint(9)
                .setOptions(List.of("Hello", "Me", "You", "Him"))
                .setAnswer("Me");

        List<Question> questions = new ArrayList<>(getExamQuestions());
        questions.add(newQ);
        exam
                .setExamNumber("A202")
                .setName("Art")
                .setQuestions(questions)
                .setTimed(false);

        MvcResult updatedResult = mockMvc.perform(
                        put(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(exam))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isString())
                .andReturn();

        String updatedContent = updatedResult.getResponse().getContentAsString();
        Exam updatedExam = mapFromJson(updatedContent, Exam.class);

        assertEquals(updatedExam.getId(), exam.getId());
        assertNotEquals(updatedExam.getExamNumber(), newExam.getExamNumber());
        assertNotEquals(updatedExam.getName(), newExam.getName());
        assertNotEquals(updatedExam.getQuestions().size(), newExam.getQuestions().size());
        assertNotEquals(updatedExam.isTimed(), newExam.isTimed());
    }
    
    @Test
    @WithMockUser(username = "johndoe@cbt.com", password = "johnnydoe", authorities = {"CANDIDATE"})
    void registerUserTest() throws Exception {
        ExamRequest newExam = getExam();
        examRepository.save(new Exam()
                                    .setExamNumber(newExam.getExamNumber())
                                    .setCandidates(
                                            newExam.getCandidates().stream()
                                                   .map(r -> {
                                                       return userRepository.findUserByEmail(r).orElseThrow(
                                                               () -> new ResourceNotFoundException("Error adding the candidates for exam"));
                                                   })
                                                    .collect(Collectors.toSet())
                                    ));
        User john = userRepository.save(new User()
                                    .setEmail("johndoe@cbt.com")
                                    .setPassword("johnnydoe")
                                    .setRole(Role.CANDIDATE));
        MvcResult result = mockMvc.perform(
                                          post(URI+"/register/"+newExam.getExamNumber())
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content("")
                                                  .accept(MediaType.APPLICATION_JSON))
                                  .andReturn();
        Exam exam = mapFromJson(result.getResponse().getContentAsString(), Exam.class);
        assertNotNull(exam);
        assertThat(exam.getCandidates().size()).isEqualTo(3);
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

    private ExamRequest getExam(){
        return new ExamRequest()
                .setExamNumber("N101")
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
                        add("aalex@cbt.com");
                        add("bobreed@cbt.com");
                    }
                });
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T mapFromJson(String json, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.readValue(json, clazz);
    }
}