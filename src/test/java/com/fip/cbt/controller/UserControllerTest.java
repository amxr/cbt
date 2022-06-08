//package com.fip.cbt.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fip.cbt.CbtApplication;
//import com.fip.cbt.controller.request.UserLoginRequest;
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
//        MvcResult result = mockMvc.perform(
//                                          post(URI+"/register")
//                                                  .contentType(MediaType.APPLICATION_JSON)
//                                                  .content(mapToJson(alice))
//                                                  .accept(MediaType.APPLICATION_JSON))
//                                  .andExpect(status().isCreated())
//                                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                                  .andExpect(jsonPath("$.id").isString())
//                                  .andExpect(jsonPath("$.name").value(alice.getName()))
//                                  .andReturn();
//
//        mockMvc.perform(
//                       post(URI+"/register")
//                               .contentType(MediaType.APPLICATION_JSON)
//                               .content(mapToJson(alice))
//                               .accept(MediaType.APPLICATION_JSON))
//               .andExpect(status().isConflict())
//               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//               .andExpect(jsonPath("$.error_message").value("409 CONFLICT \"User exist!\""))
//               .andExpect(jsonPath("$.error_code").value("409 CONFLICT"));
//
//        User aliceResult = mapFromJson(result.getResponse().getContentAsString(), User.class);
//        assertNotNull(aliceResult);
//        assertEquals(aliceResult.getName(), alice.getName());
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
//        UserLoginRequest aliceLoginRequest = new UserLoginRequest()
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
