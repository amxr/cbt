package com.fip.cbt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fip.cbt.CbtApplication;
import com.fip.cbt.controller.request.ExamRequest;
import com.fip.cbt.dto.mapper.ExamMapper;
import com.fip.cbt.model.Exam;
import com.fip.cbt.model.Question;
import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.ExamRepository;
import com.fip.cbt.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CbtApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final String URI = "/api/v1/exam";

    @BeforeAll
    void beforeAll(){
        userRepository.deleteAll();
        User flexisaf = new User()
                .setEmail("admin@flexisaf.com")
                .setPassword(encoder.encode("administrator"))
                .setName("flexisaf")
                .setRole(Role.TESTOWNER)
                .setEnabled(true);
        User alice = new User()
                .setName("Alice Alex")
                .setEmail("aalex@cbt.com")
                .setPassword(encoder.encode("aliceAlex123"))
                .setRole(Role.CANDIDATE)
                .setEnabled(true);
        User bob = new User()
                .setName("Robert Reed")
                .setEmail("bobreed@cbt.com")
                .setPassword(encoder.encode("bobbyreeder12"))
                .setRole(Role.CANDIDATE)
                .setEnabled(true);
        userRepository.saveAll(List.of(alice, bob, flexisaf));
    }
    
    @AfterEach
    void tearDown() {
        examRepository.deleteAll();
    }

    @AfterAll
    void afterAll(){
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin@flexisaf.com", password = "administrator", authorities = {"TESTOWNER"})
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
        assertThat(exam.getCandidates()).isNullOrEmpty();
    }

    @Test
    @WithMockUser(username = "admin@flexisaf.com", password = "administrator", authorities = {"TESTOWNER"})
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
    @WithMockUser(username = "admin@flexisaf.com", password = "administrator", authorities = {"TESTOWNER"})
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
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Exam exam = mapFromJson(content, Exam.class);

        assertEquals(exam.getExamNumber(), newExam.getExamNumber());
        assertEquals(exam.getQuestions().size(), newExam.getQuestions().size());
        assertEquals(exam.getName(), newExam.getName());
    }

    @Test
    @WithMockUser(username = "admin@flexisaf.com", password = "administrator", authorities = {"TESTOWNER"})
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
    @WithMockUser(username = "admin@flexisaf.com", password = "administrator", authorities = {"TESTOWNER"})
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
    @WithMockUser(username = "aalex@cbt.com", password = "aliceAlex123", authorities = {"CANDIDATE"})
    void userRegistrationTest() throws Exception {
        ExamRequest newExam = getExam();
        examRepository.save(new Exam()
                                    .setExamNumber(newExam.getExamNumber()));

        MvcResult result = mockMvc.perform(
                patch(URI+"/"+newExam.getExamNumber()+"/register/"))
                .andReturn();

        Exam exam = mapFromJson(result.getResponse().getContentAsString(), Exam.class);
        assertNotNull(exam);
        assertThat(exam.getRegisteredCandidates().size()).isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "admin@flexisaf.com", password = "administrator", authorities = {"TESTOWNER"})
    void approveCandidatesTest() throws Exception {
        User charlie = new User().setName("Charles Cousy")
                             .setEmail("ccousy@cbt.com")
                             .setPassword("ccousy12")
                             .setRole(Role.CANDIDATE);
        User dave = new User().setName("David Dune")
                             .setEmail("davedune@cbt.com")
                             .setPassword("davedune12")
                             .setRole(Role.CANDIDATE);

        List<User> users = userRepository.saveAll(List.of(charlie, dave));
        User alice = userRepository.findUserByEmail("aalex@cbt.com").orElseThrow();
        User bob = userRepository.findUserByEmail("bobreed@cbt.com").orElseThrow();
        Set<User> usersSet = Set.of(users.get(0), users.get(1), alice, bob);

       User flexisaf = userRepository.findUserByEmail("admin@flexisaf.com").orElseThrow();

        ExamRequest newExam = getExam();
        Exam exam = ExamMapper.toExam(newExam)
                .setRegisteredCandidates(usersSet)
                .setCandidates(Collections.emptySet())
                .setOwner(flexisaf);

        Exam savedExam = examRepository.save(exam);

        assertThat(savedExam.getRegisteredCandidates().size()).isEqualTo(4);

        Set<String> approvedCandidates = Set.of("bobreed@cbt.com", "ccousy@cbt.com");

        MvcResult result = mockMvc.perform(
                                          patch(URI+"/"+newExam.getExamNumber()+"/candidates/approve")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(mapToJson(approvedCandidates))
                                                  .accept(MediaType.APPLICATION_JSON))
                                  .andReturn();

        Exam examResult = mapFromJson(result.getResponse().getContentAsString(), Exam.class);
        assertNotNull(examResult);
        assertThat(examResult.getCandidates().size()).isEqualTo(2);
        assertThat(examResult.getRegisteredCandidates().size()).isEqualTo(2);
    }

//    @Test
//    void setContainsTest(){
//        ExamRequest newExam = getExam();
//        Set<String> containedCandidate = new HashSet<>(){
//            {
//                add("aalex@cbt.com");
//            }
//        };
//        assertThat(containedCandidate.equals(newExam.getCandidateRequests())).isFalse();
//
//        Set<String> notContainedCandidate = new HashSet<>(){
//            {
//                add("johndoe@cbt.com");
//            }
//        };
//        assertThat(notContainedCandidate.equals(newExam.getCandidateRequests())).isFalse();
//
//        Set<String> containsAllCandidates = new HashSet<>(){
//            {
//                add("aalex@cbt.com");
//                add("bobreed@cbt.com");
//            }
//        };
//        assertThat(containsAllCandidates.equals(newExam.getCandidateRequests())).isTrue();
//
//    }
//
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
                .setOpen(true);
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