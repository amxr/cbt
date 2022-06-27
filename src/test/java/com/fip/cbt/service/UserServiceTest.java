//package com.fip.cbt.service;
//
//import com.fip.cbt.controller.request.SignUpRequest;
//import com.fip.cbt.controller.request.LoginCredentials;
//import com.fip.cbt.dto.UserDto;
//import com.fip.cbt.dto.mapper.UserMapper;
//import com.fip.cbt.model.Role;
//import com.fip.cbt.model.User;
//import com.fip.cbt.repository.UserRepository;
//import com.fip.cbt.service.impl.UserService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//    @Mock
//    UserRepository userRepository;
//
//    @Autowired
//    @InjectMocks
//    UserService userService;
//
//    @Spy
//    PasswordEncoder encoder = new BCryptPasswordEncoder();
//
//    @Spy
//    UserMapper mapper = new UserMapper();
//
//    @AfterEach
//    void tearDown(){
//        userRepository.deleteAll();
//    }
//    @Test
//    public void createAndLoadUserByUsername(){
//        String email = "bobreed@cbt.com";
//
//        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(new User()));
//
//        UserDetails loadUserByUsername = userService.loadUserByUsername(email);
//
//        assertThat(loadUserByUsername).isNotNull();
//    }
//
//    @Test
//    public void saveUserTest(){
//        SignUpRequest bobRequest = new SignUpRequest()
//                .setName("Robert Reed")
//                .setEmail("bobreed@cbt.com")
//                .setPassword("bobbyreeder12");
//
//        User bobUser = mapper.toUser(bobRequest);
//        when(userRepository.save(any(User.class))).thenReturn(bobUser);
//
//        UserDto savedUser = userService.saveUser(bobRequest);
//        assertThat(savedUser.getEmail()).isEqualTo("bobreed@cbt.com");
//    }
//
//    @Test
//    public void getUserDetailsTest(){
//        LoginCredentials bobLoginRequest = new LoginCredentials()
//                .setEmail("bobreed@cbt.com")
//                .setPassword("bobbyreeder12");
//
//        User bob = new User()
//                         .setEmail("bobreed@cbt.com")
//                         .setPassword(encoder.encode("bobbyreeder12"));
//
//        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(bob));
//
//        UserDto loggedInUser = userService.getUserDetails(bobLoginRequest);
//        assertThat(loggedInUser.getEmail()).isEqualTo(bob.getUsername());
//    }
//
//    @Test
//    public void getAllTest(){
//        User alice = new User()
//                .setName("Alice Alex")
//                .setEmail("aalex@cbt.com")
//                .setPassword("aliceAlex123");
//        User bob = new User()
//                .setName("Robert Reed")
//                .setEmail("bobreed@cbt.com")
//                .setPassword("bobbyreeder12");
//        User admin = new User()
//                .setName("ADMINISTRATOR")
//                .setEmail("admin@cbt.com")
//                .setPassword("admin12")
//                .setRole(Role.ADMINISTRATOR);
//
//        when(userRepository.findAll()).thenReturn(List.of(alice, bob));
//        List<UserDto> users = userService.getAll();
//
//        assertThat(users.size()).isEqualTo(2);
//    }
//}
