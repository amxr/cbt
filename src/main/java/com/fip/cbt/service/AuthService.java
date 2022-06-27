package com.fip.cbt.service;

import com.fip.cbt.controller.request.LoginCredentials;
import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.dto.UserDto;

public interface AuthService {
    void register(SignUpRequest signUpRequest);

    UserDto login(LoginCredentials loginCredentials);
}
