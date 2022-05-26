package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.NewUserRequest;
import com.fip.cbt.controller.request.UserLoginRequest;
import com.fip.cbt.dto.UserDto;
import com.fip.cbt.dto.mapper.UserMapper;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper mapper;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));
    }

    public UserDto saveUser(NewUserRequest newUserRequest) {
        if(userRepository.findUserByEmail(newUserRequest.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User exist!");
        }

        User user = mapper.toUser(newUserRequest);
        user.setPassword(encoder.encode(newUserRequest.getPassword()));
        User savedUser = userRepository.save(user);

        return mapper.toUserDto(savedUser);
    }

    public UserDto getUserDetails(UserLoginRequest userLoginRequest) {
        User user = userRepository.findUserByEmail(userLoginRequest.getEmail().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));

        if(!encoder.matches(userLoginRequest.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect credentials.");
        }

        return mapper.toUserDto(user);
    }

    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());
    }
}
