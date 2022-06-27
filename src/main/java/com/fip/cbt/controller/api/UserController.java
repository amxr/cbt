package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.controller.request.LoginCredentials;
import com.fip.cbt.dto.UserDto;
import com.fip.cbt.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class UserController {
    @Autowired
    private UserService userService;


//    @PostMapping
//    @Operation(summary = "This is used to authenticate a user (login)")
//    public UserDto getUserDetails(@RequestBody @Valid LoginCredentials loginCredentials){
//        return userService.getUserDetails(loginCredentials);
//    }

    @GetMapping
    @SecurityRequirement(name = "cbt")
    @Operation(summary = "This is used to get all users detail (for admin)")
    public List<UserDto> getAll(){
        return userService.getAll();
    }
}
