package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.LoginCredentials;
import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.security.jwt.JWTToken;
import com.fip.cbt.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "make a new account", description = "This is used to register a new user")
    public JWTToken register(@RequestBody SignUpRequest signUpRequest){
        return authService.register(signUpRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "login")
    public JWTToken login(@RequestBody LoginCredentials loginCredentials){
        return authService.login(loginCredentials);
    }

    @GetMapping("/refresh-token")
    @Operation(summary = "refresh access token", description = "send request with refresh token to receive new access and refresh tokens")
    public JWTToken refresh(){
        return authService.refresh();
    }
}