package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.LoginCredentials;
import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.dto.UserDto;
import com.fip.cbt.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "This is used to register a new user")
    public void register(@RequestBody SignUpRequest signUpRequest){
        authService.register(signUpRequest);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginCredentials loginCredentials){
        return authService.login(loginCredentials);
    }
}