package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.controller.request.LoginCredentials;
import com.fip.cbt.dto.UserDto;
import com.fip.cbt.dto.mapper.ExamMapper;
import com.fip.cbt.dto.mapper.UserMapper;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.exception.ResourceNotFoundException;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmailIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not authenticated."));
    }

//    public UserDto getUserDetails(LoginCredentials loginCredentials) {
//        User user = userRepository.findUserByEmailIgnoreCase(loginCredentials.getEmail().toLowerCase())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));
//
//        if(!encoder.matches(loginCredentials.getPassword(), user.getPassword())){
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect credentials.");
//        }
//
//        return UserMapper.toUserDto(user);
//    }

    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public User getUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByEmailIgnoreCase(userDetails.getUsername()).orElseThrow();
    }
}
