package com.fip.cbt.service;

import com.fip.cbt.controller.request.LoginCredentials;
import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.security.jwt.JWTToken;

public interface AuthService {
    JWTToken register(SignUpRequest signUpRequest);

    JWTToken login(LoginCredentials loginCredentials);
}
