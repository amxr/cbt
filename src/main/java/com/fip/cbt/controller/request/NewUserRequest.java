package com.fip.cbt.controller.request;

import com.fip.cbt.model.Role;
import com.fip.cbt.validator.EnumNamePattern;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class NewUserRequest {
    @NotBlank(message = "Name cannot be blank!")
    private String name;

    @NotBlank(message = "email cannot be empty.")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Incorrect email format")
    private String email;

    @NotBlank(message = "password cannot be blank!")
    @Size(min = 6, max = 16, message = "Password length must be between 6 and 16")
    private String password;

    @EnumNamePattern(regexp = "TESTOWNER|CANDIDATE")
    private Role role;
}
