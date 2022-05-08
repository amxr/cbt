package com.fip.cbt.dto;

import com.fip.cbt.model.Role;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
    private String id;
    private String name;
    private String email;
    private Role role;
}
