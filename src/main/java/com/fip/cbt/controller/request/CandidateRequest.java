package com.fip.cbt.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fip.cbt.model.Role;
import com.fip.cbt.model.Test;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidateRequest {

    @NotBlank(message = "Username cannot be blank!")
    private String username;

    @NotBlank(message = "Password cannot be blank!")
    private String password;

    @NotBlank(message = "First name cannot be blank!")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank!")
    private String lastName;

    //@NotEmpty(message = "")
    private List<Role> roles;

    //@NotEmpty(message = "Questions cannot be empty!")
    private Set<@Valid Test> testsTaken;
}
