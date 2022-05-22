//package com.fip.cbt.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fip.cbt.CbtApplication;
//import com.fip.cbt.controller.request.ExamRequest;
//import com.fip.cbt.controller.request.ExamTakenRequest;
//import com.fip.cbt.exception.ResourceNotFoundException;
//import com.fip.cbt.dto.mapper.ExamMapper;
//import com.fip.cbt.model.*;
//import com.fip.cbt.repository.ExamRepository;
//import com.fip.cbt.repository.ExamTakenRepository;
//import com.fip.cbt.repository.UserRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CbtApplication.class})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class ExamTakenControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ExamTakenRepository examTakenRepository;
//
//    @Autowired
//    private ExamRepository examRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    PasswordEncoder encoder;
//
//    private final String URI = "/api/v1/exam/taken";
//
//    @BeforeAll
//    void beforeAll(){
//        userRepository.deleteAll();
//        User admin = new User()
//                .setName("Admin")
//                .setEmail("admin@cbt.com")
//                .setPassword(encoder.encode("administrator"))
//                .setEnabled(true)
//                .setRole(Role.ADMINISTRATOR);
//        User alice = new User().setName("Alice Alex")
//                .setEmail("aalex@cbt.com")
//                .setPassword(encoder.encode("aliceAlex123"))
//                .setEnabled(true)
//                .setRole(Role.CANDIDATE);
//        userRepository.saveAll(List.of(alice,admin));
//
//        Exam examN101 = ExamMapper.toExam(getExam("N101",List.of("aalex@cbt.com")));
//        Exam examN102 = ExamMapper.toExam(getExam("N102",List.of("aalex@cbt.com")));
//        examRepository.saveAll(List.of(examN101,examN102));
//    }
//
//    @AfterEach
//    void tearDown(){
//        examTakenRepository.deleteAll();
//    }
//
//    @AfterAll
//    void afterAll(){
//        examRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Test
//    @WithMockUser(username = "aalex@cbt.com", password = "aliceAlex123", authorities = {"CANDIDATE"})
//    public void submitTest() throws Exception {
//        Exam exam = examRepository.findExamByExamNumber("N101")
//                                  .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//        ExamTakenRequest examTakenRequest = getExamTaken(exam.getId());
//
//        MvcResult result = mockMvc.perform(
//                                          post(URI)
//                                                  .contentType(MediaType.APPLICATION_JSON)
//                                                  .content(mapToJson(examTakenRequest))
//                                                  .accept(MediaType.APPLICATION_JSON))
//                                  .andExpect(status().isCreated())
//                                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                                  .andExpect(jsonPath("$.id").isString())
//                                  .andExpect(jsonPath("$.exam.id").value(examTakenRequest.getExamId()))
//                                  .andReturn();
//
//        mockMvc.perform(
//                       post(URI)
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(examTakenRequest))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andExpect(status().isForbidden())
//               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//               .andExpect(jsonPath("$.error_message").value("403 FORBIDDEN \"User can't submit an exam more than once\""))
//               .andExpect(jsonPath("$.error_code").value("403 FORBIDDEN"));
//
//        ExamTaken examTaken = mapFromJson(result.getResponse().getContentAsString(), ExamTaken.class);
//        assertNotNull(examTaken);
//        assertEquals(examTaken.getExam().getExamNumber(), exam.getExamNumber());
//    }
//
//    @Test
//    //@WithMockUser(username = "aalex@cbt.com", password = "aliceAlex123", authorities = {"CANDIDATE"})
//    @WithMockUser(username = "aalex@cbt.com", password = "aliceAlex123", authorities = {"CANDIDATE"})
//    public void getAllWithNoParametersTest() throws Exception{
//        Exam exam1 = examRepository.findExamByExamNumber("N101")
//                                  .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//        Exam exam2 = examRepository.findExamByExamNumber("N102")
//                                  .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//
//        ExamTakenRequest examTakenRequest1 = getExamTaken(exam1.getId());
//        ExamTakenRequest examTakenRequest2 = getExamTaken(exam2.getId());
//
//        mockMvc.perform(
//                       post(URI)
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(examTakenRequest1))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andReturn();
//        mockMvc.perform(
//                       post(URI)
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(examTakenRequest2))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andReturn();
//
//        MvcResult result = mockMvc.perform(get(URI))
//                                  .andExpect(status().isOk())
//                                  .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        ExamTaken[] examsTaken = mapFromJson(content, ExamTaken[].class);
//        assertEquals(2, examsTaken.length);
//    }
//    @Test
//    //@WithMockUser(username = "aalex@cbt.com", password = "aliceAlex123", authorities = {"CANDIDATE"})
//    @WithMockUser(username = "admin@cbt.com", password = "administrator", authorities = {"ADMINISTRATOR"})
//    public void getAllWithUserParameterTest() throws Exception{
//        //Not working
//        Exam exam1 = examRepository.findExamByExamNumber("N101")
//                                  .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//        Exam exam2 = examRepository.findExamByExamNumber("N102")
//                                  .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//
//        User alice = userRepository.findUserByEmail("aalex@cbt.com").orElseThrow();
//
//        ExamTaken examTaken1 = examTakenRepository.save(
//                new ExamTaken()
//                        .setExam(exam1)
//                        .setUser(alice)
//                        .setResponses(new ArrayList<>(){{
//                            add(new QuestionResponse().setUserChoice("Me")
//                                                      .setText("How are you?")
//                                                      .setAnswer("Me")
//                                                      .setOptions(List.of("Hello", "Me", "You", "Him"))
//                                                      .setPoint(5)
//                            );
//                            add(new QuestionResponse().setUserChoice("Him")
//                                                      .setText("Who is she?")
//                                                      .setAnswer("Her")
//                                                      .setOptions(List.of("Her", "Him", "They", "Them"))
//                                                      .setPoint(3)
//                            );
//                            add(new QuestionResponse().setUserChoice("it")
//                                                      .setText("Who is he?")
//                                                      .setAnswer("It")
//                                                      .setOptions(List.of("You", "Me", "Them", "It"))
//                                                      .setPoint(2)
//                            );
//                        }})
//                        .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0)));
//        ExamTaken examTaken2 = examTakenRepository.save(
//                new ExamTaken()
//                        .setExam(exam2)
//                        .setUser(alice)
//                        .setResponses(new ArrayList<>(){{
//                            add(new QuestionResponse().setUserChoice("Me")
//                                                      .setText("How are you?")
//                                                      .setAnswer("Me")
//                                                      .setOptions(List.of("Hello", "Me", "You", "Him"))
//                                                      .setPoint(5)
//                            );
//                            add(new QuestionResponse().setUserChoice("Him")
//                                                      .setText("Who is she?")
//                                                      .setAnswer("Her")
//                                                      .setOptions(List.of("Her", "Him", "They", "Them"))
//                                                      .setPoint(3)
//                            );
//                            add(new QuestionResponse().setUserChoice("it")
//                                                      .setText("Who is he?")
//                                                      .setAnswer("It")
//                                                      .setOptions(List.of("You", "Me", "Them", "It"))
//                                                      .setPoint(2)
//                            );
//                        }})
//                        .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0)));
//
//        MvcResult result = mockMvc.perform(
//                get(URI)
//                        .param("user","aalex@cbt.com"))
//                                  .andExpect(status().isOk())
//                                  .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        ExamTaken[] examsTaken = mapFromJson(content, ExamTaken[].class);
//        assertEquals(2, examsTaken.length);
//    }
//
//    @Test
//    @WithMockUser(username = "aalex@cbt.com", password = "aliceAlex123", authorities = {"CANDIDATE"})
//    public void getAllWithExamParameterTest() throws Exception{
//        Exam exam1 = examRepository.findExamByExamNumber("N101")
//                                  .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//        Exam exam2 = examRepository.findExamByExamNumber("N102")
//                                  .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//
//        ExamTakenRequest examTakenRequest1 = getExamTaken(exam1.getId());
//        ExamTakenRequest examTakenRequest2 = getExamTaken(exam2.getId());
//
//        mockMvc.perform(
//                       post(URI)
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(examTakenRequest1))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andReturn();
//        mockMvc.perform(
//                       post(URI)
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(examTakenRequest2))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andReturn();
//
//        MvcResult result = mockMvc.perform(
//                get(URI)
//                        .param("exam","N101"))
//                                  .andExpect(status().isOk())
//                                  .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        ExamTaken[] examsTaken = mapFromJson(content, ExamTaken[].class);
//        assertEquals(2, examsTaken.length); //For 2 candidates?
//    }
//
//    @Test
//    @WithMockUser(username = "aalex@cbt.com", password = "aliceAlex123", authorities = {"CANDIDATE"})
//    public void getAllWithUserAndExamParametersTest() throws Exception{
//        Exam exam1 = examRepository.findExamByExamNumber("N101")
//                                  .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//        Exam exam2 = examRepository.findExamByExamNumber("N102")
//                                  .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//
//        ExamTakenRequest examTakenRequest1 = getExamTaken(exam1.getId());
//        ExamTakenRequest examTakenRequest2 = getExamTaken(exam2.getId());
//
//        mockMvc.perform(
//                       post(URI)
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(examTakenRequest1))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andReturn();
//        mockMvc.perform(
//                       post(URI)
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(examTakenRequest2))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andReturn();
//
//        MvcResult result = mockMvc.perform(
//                get(URI)
//                        .param("user","aalex@cbt.com")
//                        .param("exam", "N101"))
//                                  .andExpect(status().isOk())
//                                  .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        ExamTaken[] examsTaken = mapFromJson(content, ExamTaken[].class);
//        assertEquals(2, examsTaken.length);
//    }
//
//    @Test
//    @WithMockUser(username = "aalex@cbt.com", password = "aliceAlex123", authorities = {"CANDIDATE"})
//    public void getOneTest() throws Exception {
//        Exam exam1 = examRepository.findExamByExamNumber("N101")
//                                   .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//
//        ExamTakenRequest examTakenRequest = getExamTaken(exam1.getId());
//
//        //submit exam
//        MvcResult submitExam = mockMvc.perform(
//                       post(URI)
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(examTakenRequest))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andReturn();
//        //parse exam
//        String content = submitExam.getResponse().getContentAsString();
//        ExamTaken submittedExamTaken = mapFromJson(content, ExamTaken.class);
//
//        //then test 'getOne' function
//        MvcResult result = mockMvc.perform(
//                                          get(URI+"/"+submittedExamTaken.getId()))
//                                  .andExpect(status().isOk())
//                                  .andReturn();
//
//        String resultContent = result.getResponse().getContentAsString();
//        ExamTaken returnedExamTaken = mapFromJson(resultContent, ExamTaken.class);
//        assertEquals(returnedExamTaken.getExam().getExamNumber(), submittedExamTaken.getExam().getExamNumber());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", password = "admin", authorities = {"ADMINISTRATOR"})
//    //@WithMockUser(username = "aalex@cbt.com", password = "aliceAlex123", authorities = {"CANDIDATE"})
//    public void deleteTest() throws Exception {
//        //Not working
//        Exam exam1 = examRepository.findExamByExamNumber("N101")
//                                   .orElseThrow(()->new ResourceNotFoundException("No such exam"));
//
//        ExamTaken examTaken = examTakenRepository.save(
//                new ExamTaken()
//                        .setExam(exam1)
//                        .setResponses(new ArrayList<>(){{
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
//                        .setUserStartTime(LocalDateTime.of(1992, 12, 12, 12, 0)));
//
//        mockMvc.perform(
//                      delete(URI+"/"+ examTaken.getId()))
//              .andExpect(status().isOk());
//
//
//         mockMvc.perform(get(URI+"/"+ examTaken.getId()))
//                                  .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error_message").value("Exam with number " + examTaken.getId() + " not found."))
//                .andExpect(jsonPath("$.error_code").value("NOT_FOUND"));
//
//    }
//
//    private String mapToJson(Object obj) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//        return objectMapper.writeValueAsString(obj);
//    }
//
//    private <T> T mapFromJson(String json, Class<T> clazz) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//        return objectMapper.readValue(json, clazz);
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
//                        addAll(candidateEmails);
//                        //add("aalex@cbt.com");
//                        //add("bobreed@cbt.com");
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
//    private ExamTakenRequest getExamTaken(String id){
//        return new ExamTakenRequest()
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
