package com.fip.cbt.controller.request;

import com.fip.cbt.model.Role;
import com.fip.cbt.validator.EnumNamePattern;
import com.fip.cbt.validator.PasswordMatches;
import com.fip.cbt.validator.ValidPassword;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@PasswordMatches
public class SignUpRequest {
    @NotBlank(message = "Name cannot be blank!")
    private String name;

    @NotBlank(message = "email cannot be empty.")
    @Email(message = "Incorrect email format")
    private String email;

    @NotBlank(message = "password cannot be blank!")
    @ValidPassword
    private String password;

    @NotBlank(message = "passwords must match!")
    @ValidPassword
    private String matchingPassword;

    @EnumNamePattern(regexp = "TESTOWNER|CANDIDATE")
    private Role role;
}
