package com.fip.cbt.service.impl;

import com.fip.cbt.controller.request.LoginCredentials;
import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.dto.mapper.UserMapper;
import com.fip.cbt.exception.ResourceAlreadyExistsException;
import com.fip.cbt.model.User;
import com.fip.cbt.repository.UserRepository;
import com.fip.cbt.security.jwt.JWTToken;
import com.fip.cbt.security.jwt.JWTUtil;
import com.fip.cbt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    @Override
    public JWTToken register(SignUpRequest signUpRequest) {
        if(userRepository.findByEmailIgnoreCase(signUpRequest.getEmail()).isPresent()){
            throw new ResourceAlreadyExistsException("User exist!");
        }

        User user = UserMapper.toUser(signUpRequest);
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);

        return jwtUtil.generateTokens(signUpRequest.getEmail());
    }

    @Override
    public JWTToken login(LoginCredentials loginCredentials) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(), loginCredentials.getPassword());

        authManager.authenticate(authToken);

        return jwtUtil.generateTokens(loginCredentials.getEmail());
    }
}
