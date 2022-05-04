package com.fip.cbt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fip.cbt.controller.api.CandidateController;
import com.fip.cbt.controller.request.CandidateRequest;
import com.fip.cbt.mapper.CandidateMapper;
import com.fip.cbt.model.Candidate;
import com.fip.cbt.service.CandidateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CbtApplication.class})
//@WebMvcTest(CandidateController.class)

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class CandidateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;
    
    @InjectMocks
    private CandidateController candidateController;
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    private final String URI = "/api/v1/candidates";

    //@After
    //public void tearDown() {
    //    candidateRepository.deleteAll();
    //}
    /*@Before
    public void setUp(){
        CandidateRequest alice = new CandidateRequest()
                .setFirstName("Alice")
                .setLastName("Alex")
                .setUsername("aalex")
                .setPassword("aliceAlex123");
        CandidateRequest bob = new CandidateRequest()
                .setFirstName("Robert")
                .setLastName("Reed")
                .setUsername("bobreed")
                .setPassword("bobbyreeder12");
    }*/

    @Test
    public void getAllCandidatesTest() throws Exception{
        CandidateRequest alice = new CandidateRequest()
                .setFirstName("Alice")
                .setLastName("Alex")
                .setUsername("aalex")
                .setPassword("aliceAlex123");
        CandidateRequest bob = new CandidateRequest()
                .setFirstName("Robert")
                .setLastName("Reed")
                .setUsername("bobreed")
                .setPassword("bobbyreeder12");
        
        when(candidateService.addCandidate(any(CandidateRequest.class)))
                .thenReturn(CandidateMapper.toCandidate(alice));
        when(candidateService.addCandidate(any(CandidateRequest.class)))
                .thenReturn(CandidateMapper.toCandidate(bob));
        
        mockMvc.perform(
                        post(URI+"/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(alice))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(
                        post(URI+"/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bob))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        MvcResult result = mockMvc.perform(get(URI)).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        String content = result.getResponse().getContentAsString();
        Candidate[] candidatesList = objectMapper.readValue(content, Candidate[].class);
        assertEquals(2, candidatesList.length);
        //System.out.println(content);

        //MvcResult result2 = mockMvc.perform(get(URI + "?students=true")).andReturn();
    }
    @Test
    public void addNewCandidateTest() throws Exception {
        CandidateRequest alice = new CandidateRequest()
                .setFirstName("Alice")
                .setLastName("Alex")
                .setUsername("aalex")
                .setPassword("aliceAlex123");

        mockMvc.perform(
                       post(URI)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(objectMapper.writeValueAsString(alice))
                               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.username").isString())
               .andExpect(jsonPath("$.username").value(alice.getUsername()));
        
        mockMvc.perform(
                       post(URI)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(objectMapper.writeValueAsString(alice))
                               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isConflict())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorMessage").value("Department with name [" + alice.getUsername() + "] already exists."))
               .andExpect(jsonPath("$.errorCode").value("CONFLICT"));
    }
    
    @Test
    public void deleteCandidateTest() throws Exception {
        CandidateRequest alice = new CandidateRequest()
                .setFirstName("Alice")
                .setLastName("Alex")
                .setUsername("aalex")
                .setPassword("aliceAlex123");
        
        MvcResult result = mockMvc.perform(
                                          post(URI)
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(alice))
                                                  .accept(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isNoContent())
                                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                  .andReturn();
        
        String content = result.getResponse().getContentAsString();
        Candidate aliceResponse = objectMapper.readValue(content, Candidate.class);
        assertNotNull(aliceResponse);
        
        mockMvc.perform(
                       delete(URI+"/"+aliceResponse.getUsername()))
               .andExpect(status().isOk());
    }
}
