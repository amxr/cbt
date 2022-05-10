package com.fip.cbt.repository;

import com.fip.cbt.model.Candidate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@DataJpaTest
@DataMongoTest
@ExtendWith(MockitoExtension.class)
//@RunWith(SpringRunner.class)
public class CandidateRepositoryTest {
    @Autowired
    CandidateRepository candidateRepository;

    @BeforeEach
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
    }

    @Test
    public void createAndDeleteCandidate(){
        //given

        //when
        List<Candidate> candidates = candidateRepository.findAll();

        //then
        assertThat(candidates.size()).isEqualTo(2);

        //when
        candidateRepository.deleteAll();
        List<Candidate> candidates1 = candidateRepository.findAll();

        //then
        assertThat(candidates1.size()).isEqualTo(0);
    }
    @Test
    public void findByUsernameTest(){
        assertThat(candidateRepository.count()).isEqualTo(2);

        Optional<Candidate> alice = candidateRepository.findByUsername("aalex");

        assertThat(alice.get().getUsername()).isEqualTo("aalex");

        Optional<Candidate> doesNotExist = candidateRepository.findByUsername("alex");

        assertThat(doesNotExist).isEmpty();
    }
    @AfterEach
    public void tearDown(){
        candidateRepository.deleteAll();
    }
}
