package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.LoginCredentials;
import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.dto.UserDto;
import com.fip.cbt.dto.mapper.UserMapper;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.UserRepository;
import com.fip.cbt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void register(SignUpRequest signUpRequest) {
        if(userRepository.findUserByEmailIgnoreCase(signUpRequest.getEmail()).isPresent()){
            throw new ResourceAlreadyExistsException("User exist!");
        }

        User user = UserMapper.toUser(signUpRequest);
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        User savedUser = userRepository.save(user);
    }

    @Override
    public UserDto login(LoginCredentials loginCredentials) {
        User user = userRepository.findUserByEmailIgnoreCase(loginCredentials.getEmail().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));

        if(!encoder.matches(loginCredentials.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect credentials.");
        }

        return UserMapper.toUserDto(user);
    }
}
