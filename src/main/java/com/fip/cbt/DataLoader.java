package com.fip.cbt;

import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User initialUser = new User()
                .setName("Admin")
                .setEmail("admin@cbt.com")
                .setPassword(encoder.encode("administrator"))
                .setRole(Role.ADMINISTRATOR);

        if(!userRepository.findUserByEmail(initialUser.getUsername()).isPresent()){
            userRepository.save(initialUser);
        }
    }
}
