package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.NewUserRequest;
import com.fip.cbt.controller.request.UserLoginRequest;
import com.fip.cbt.dto.UserDto;
import com.fip.cbt.model.User;
import com.fip.cbt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody @Valid NewUserRequest newUserRequest){
        return userService.saveUser(newUserRequest);
    }

    @PostMapping("/login")
    public UserDto getUserDetails(@RequestBody @Valid UserLoginRequest userLoginRequest){
        return userService.getUserDetails(userLoginRequest);
    }
}
