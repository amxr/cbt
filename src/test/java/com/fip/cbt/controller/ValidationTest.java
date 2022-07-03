package com.fip.cbt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fip.cbt.CbtApplication;
import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CbtApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    private final String URI = "/api/v1/auth";

    User alice;

    @BeforeAll
    void beforeAll(){
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp(){
        alice = new User().setName("Alice Alex")
                .setEmail("aalex@cbt.com")
                .setPassword("aliceAlex123")
                .setRole(Role.CANDIDATE)
                .setEnabled(true);
        userRepository.save(
                new User()
                        .setEmail("admin@cbt.com")
                        .setPassword(encoder.encode("administrator"))
                        .setRole(Role.ADMINISTRATOR)
                        .setEnabled(true)
        );
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void createAndRegisterUserTest() throws Exception {

        SignUpRequest aliceRequest = new SignUpRequest()
                .setName("Alice Alex")
                .setEmail("aalex@film.com")
                .setPassword("aliceAlex123")
                .setMatchingPassword("aliceAlex123")
                .setRole(Role.CANDIDATE);
        
        mockMvc.perform(
                          post(URI+"/register")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(mapToJson(aliceRequest))
                                  .accept(MediaType.APPLICATION_JSON))
                  .andExpect(status().isCreated());

        mockMvc.perform(
                       post(URI+"/register")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(mapToJson(aliceRequest))
                               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isConflict())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.error_message").value("User exist!"))
               .andExpect(jsonPath("$.error_code").value("CONFLICT"));

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
