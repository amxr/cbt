package com.fip.cbt.service;

import com.fip.cbt.controller.request.CandidateRequest;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.mapper.CandidateMapper;
import com.fip.cbt.model.Candidate;
import com.fip.cbt.model.Role;
import com.fip.cbt.repository.CandidateRepository;
import com.fip.cbt.repository.RoleRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class CandidateServiceImplTest {
    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private RoleRepository roleRepository;

    @Autowired
    @InjectMocks
    private CandidateServiceImpl candidateService;

    /*@Before
    public void setUp(){
        Candidate alice = new Candidate().setFirstName("Alice")
                .setLastName("Alex")
                .setUsername("aalex")
                .setPassword("aliceAlex123");
        Candidate bob = new Candidate()
                .setFirstName("Robert")
                .setLastName("Reed")
                .setUsername("bobreed")
                .setPassword("bobbyreeder12");
        candidateRepository.saveAll(List.of(alice, bob));
    }*/

    @Test
    public void addCandidate(){
        List<Role> roles = new ArrayList<>() {
            {
                add(new Role().setName("student"));
            }
        };
        CandidateRequest charlieRequest = new CandidateRequest()
                .setUsername("ccharlie")
                .setPassword("charlie123")
                .setFirstName("Charles")
                .setLastName("Cosby")
                .setRoles(roles);
        when(candidateRepository.save(any(Candidate.class))).thenReturn(CandidateMapper.toCandidate(charlieRequest));
        Candidate charlie = candidateService.addCandidate(charlieRequest);
        
        assertEquals(charlie.getUsername(), "ccharlie");
    }
    @Test
    public void getAllCandidates(){
        Candidate alice = new Candidate().setFirstName("Alice")
                                         .setLastName("Alex")
                                         .setUsername("aalex")
                                         .setPassword("aliceAlex123");
        Candidate bob = new Candidate()
                .setFirstName("Robert")
                .setLastName("Reed")
                .setUsername("bobreed")
                .setPassword("bobbyreeder12");
        
        when(candidateRepository.findAll()).thenReturn(List.of(alice, bob));
    
        assertThat(candidateRepository.findAll().size()).isEqualTo(2);
    }
    
    @Test
    public void getCandidateTest(){
        String username = "aalex";
        
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            candidateService.deleteCandidate(username);
        });
        
        assertEquals(e.getMessage(), "Could not find candidate with username " +username);
        
        when(candidateRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new Candidate()));
        
        Candidate candidate = candidateService.getCandidate(username);
        
        assertThat(candidate).isNotNull();
    }
    
    @Test
    public void deleteCandidateTest() {
        String username = "aalex";
        
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            candidateService.deleteCandidate(username);
        });
        
        assertEquals(e.getMessage(), "Could not find candidate with username " +username);
        
        when(candidateRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new Candidate()));
        
        candidateService.deleteCandidate(username);
        
        verify(candidateRepository, times(1)).delete(any(Candidate.class));
    }

    /*@After
    public void tearDown(){
        candidateRepository.deleteAll();
    }*/
}
