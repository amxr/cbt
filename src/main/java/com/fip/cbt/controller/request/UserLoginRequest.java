package com.fip.cbt.controller.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class UserLoginRequest {
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 16, message = "Password length must be between 6 and 16")
    private String password;
}
