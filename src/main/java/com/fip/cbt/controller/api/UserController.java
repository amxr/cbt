package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.SignUpRequest;
import com.fip.cbt.controller.request.LoginCredentials;
import com.fip.cbt.dto.UserDto;
import com.fip.cbt.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/info")
    public UserDto getUserDetails(){
        return userService.getUserDetails();
    }

    @GetMapping("/all")
    @SecurityRequirement(name = "cbt")
    @Operation(summary = "get all registered users",description = "This is used to get all users detail (for admin)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> getAll(){
        return userService.getAll();
    }
}
