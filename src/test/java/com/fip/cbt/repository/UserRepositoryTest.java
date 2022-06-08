package com.fip.cbt.repository;

import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void beforeAll(){
        userRepository.deleteAll();
    }
    
    @BeforeEach
    void setUp(){
        User alice = new User().setName("Alice Alex")
                               .setEmail("aalex@cbt.com")
                               .setPassword("aliceAlex123")
                               .setRole(Role.TESTOWNER);
        User bob = new User().setName("Robert Reed")
                             .setEmail("bobreed@cbt.com")
                             .setPassword("bobbyreeder1")
                             .setRole(Role.CANDIDATE);
        User charlie = new User().setName("Charles Cousy")
                             .setEmail("ccousy@cbt.com")
                             .setPassword("charliecousy")
                             .setRole(Role.ADMINISTRATOR);
        userRepository.saveAll(List.of(alice, bob,charlie));
    }
    
    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }
    
    @Test
    public void createAndDeleteUser(){
        User newUser = new User()
                .setEmail("newuser@cbt.com")
                .setPassword("newpassword")
                .setName("New User")
                .setRole(Role.CANDIDATE);
        userRepository.save(newUser);
        
        //when
        List<User> users = userRepository.findAll();
    
        //then
        assertThat(users.size()).isEqualTo(4);
    
        //when
        userRepository.deleteAll();
        List<User> usersAfterDeletion = userRepository.findAll();
    
        //then
        assertThat(usersAfterDeletion.size()).isEqualTo(0);
    }
    
    @Test
    public void findUserByEmailTest(){
        Optional<User> findUser = userRepository.findUserByEmail("aalex@cbt.com");
        assertThat(findUser).isPresent();
        assertThat(findUser.get().getName()).isEqualTo("Alice Alex");
        
        Optional<User> findNonExistentUser = userRepository.findUserByEmail("emptyuser@cbt.com");
        assertThat(findNonExistentUser).isEmpty();
    }
    
    @Test
    public void findUserByRoleTest(){
        List<User> findCandidates = userRepository.findUserByRole(Role.CANDIDATE);
        assertThat(findCandidates.size()).isEqualTo(1);
        
        List<User> findAdmin = userRepository.findUserByRole(Role.ADMINISTRATOR);
        assertThat(findAdmin.size()).isEqualTo(1);

        List<User> findTestOwner = userRepository.findUserByRole(Role.TESTOWNER);
        assertThat(findTestOwner.size()).isEqualTo(1);
    }
}
