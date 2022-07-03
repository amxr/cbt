//package com.fip.cbt.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fip.cbt.CbtApplication;
//import com.fip.cbt.controller.request.LoginCredentials;
//import com.fip.cbt.controller.request.SignUpRequest;
//import com.fip.cbt.model.Role;
//import com.fip.cbt.model.User;
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
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CbtApplication.class})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    PasswordEncoder encoder;
//
//    private final String URI = "/api/v1/auth";
//
//    User alice;
//
//    @BeforeAll
//    void beforeAll(){
//        userRepository.deleteAll();
//    }
//
//    @BeforeEach
//    void setUp(){
//        alice = new User().setName("Alice Alex")
//                .setEmail("aalex@cbt.com")
//                .setPassword("aliceAlex123")
//                .setRole(Role.CANDIDATE)
//                .setEnabled(true);
//        userRepository.save(
//                new User()
//                        .setEmail("admin@cbt.com")
//                        .setPassword(encoder.encode("administrator"))
//                        .setRole(Role.ADMINISTRATOR)
//                        .setEnabled(true)
//        );
//    }
//
//    @AfterEach
//    void tearDown() {
//        userRepository.deleteAll();
//    }
//
//    @Test
//    public void createAndRegisterUserTest() throws Exception {
//
//        SignUpRequest aliceRequest = new SignUpRequest()
//                .setName("Alice Alex")
//                .setEmail("aalex@film.com")
//                .setPassword("aliceAlex123")
//                .setMatchingPassword("aliceAlex123")
//                .setRole(Role.CANDIDATE);
//        MvcResult result = mockMvc.perform(
//                                          post(URI+"/register")
//                                                  .contentType(MediaType.APPLICATION_JSON)
//                                                  .content(mapToJson(aliceRequest))
//                                                  .accept(MediaType.APPLICATION_JSON))
//                                  .andExpect(status().isCreated())
//                                  //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                                  //.andExpect(jsonPath("$.id").isString())
//                                  //.andExpect(jsonPath("$.name").value(alice.getName()))
//                                  .andReturn();
//
//        mockMvc.perform(
//                       post(URI+"/register")
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(aliceRequest))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andExpect(status().isConflict())
//               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//               .andExpect(jsonPath("$.error_message").value("User exist!"))
//               .andExpect(jsonPath("$.error_code").value("CONFLICT"));
//
//        //User aliceResult = mapFromJson(result.getResponse().getContentAsString(), User.class);
//        //assertNotNull(aliceResult);
//        //assertEquals(aliceResult.getName(), alice.getName());
//    }
//
//    @Test
//    public void getUserDetailsTest() throws Exception {
//        mockMvc.perform(
//                      post(URI+"/register")
//                              .contentType(MediaType.APPLICATION_JSON)
//                              .content(mapToJson(alice))
//                              .accept(MediaType.APPLICATION_JSON))
//              .andExpect(status().isCreated())
//              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//              .andExpect(jsonPath("$.id").isString())
//              .andExpect(jsonPath("$.name").value(alice.getName()))
//              .andReturn();
//
//        LoginCredentials aliceLoginRequest = new LoginCredentials()
//                                           .setEmail("aalex@cbt.com")
//                                           .setPassword("aliceAlex123");
//
//        MvcResult result = mockMvc.perform(
//                                          post(URI)
//                                                  .contentType(MediaType.APPLICATION_JSON)
//                                                  .content(mapToJson(aliceLoginRequest))
//                                                  .accept(MediaType.APPLICATION_JSON))
//                                  .andExpect(status().isOk())
//                                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                                  .andExpect(jsonPath("$.id").isString())
//                                  .andExpect(jsonPath("$.email").value(aliceLoginRequest.getEmail()))
//                                  .andReturn();
//
//        User aliceResult = mapFromJson(result.getResponse().getContentAsString(), User.class);
//        assertNotNull(aliceResult);
//        assertEquals(aliceResult.getUsername(), aliceLoginRequest.getEmail());
//    }
//
//    @Test
//    @WithMockUser(username = "admin@cbt.com", password = "administrator", roles = {"ADMINISTRATOR"})
//    public void getAllUsersTest() throws Exception {
//        User bob = new User().setName("Robert Reed")
//                             .setEmail("bobreed@cbt.com")
//                             .setPassword("bobbyreeder12")
//                             .setRole(Role.CANDIDATE);
//
//        mockMvc.perform(
//                       post(URI+"/register")
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(alice))
//                               .accept(MediaType.APPLICATION_JSON));
//        mockMvc.perform(
//                       post(URI+"/register")
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(bob))
//                               .accept(MediaType.APPLICATION_JSON));
//
//        MvcResult result = mockMvc.perform(get(URI))
//                                  .andExpect(status().isOk())
//                                  .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        User[] users = mapFromJson(content, User[].class);
//        assertEquals(3, users.length);
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
//}

//List<String> emails = new ArrayList();
//        //Valid Email Ids
//        emails.add("simple@example.com");
//        emails.add("very.common@example.com");
//        emails.add("disposable.style.email.with+symbol@example.com");
//        emails.add("other.email-with-hyphen@example.com");
//        emails.add("fully-qualified-domain@example.com");
//        emails.add("user.name+tag+sorting@example.com");
//        emails.add("fully-qualified-domain@example.com");
//        emails.add("x@example.com");
//        emails.add("carlosd'intino@arnet.com.ar");
//        emails.add("example-indeed@strange-example.com");
//        emails.add("admin@mailserver1");
//        emails.add("example@s.example");
//        emails.add("\" \"@example.org");
//        emails.add("\"john..doe\"@example.org");
//
//        //Invalid emails Ids
//        emails.add("Abc.example.com");
//        emails.add("A@b@c@example.com");
//        emails.add("a\"b(c)d,e:f;g<h>i[j\\k]l@example.com");
//        emails.add("just\"not\"right@example.com");
//        emails.add("this is\"not\\allowed@example.com");
//        emails.add("this\\ still\"not\\allowed@example.com");
//        emails.add("1234567890123456789012345678901234567890123456789012345678901234+x@example.com");
//        emails.add("john..doe@example.com");
//        emails.add("john.doe@example..com");
