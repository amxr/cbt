package com.fip.cbt.dto.mapper;

import com.fip.cbt.controller.request.NewUserRequest;
import com.fip.cbt.dto.UserDto;
import com.fip.cbt.model.Role;
import com.fip.cbt.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toUserDto(User user){
        return new UserDto()
                .setId(user.getId())
                .setName(user.getName())
                .setRole(user.getRole())
                .setEmail(user.getUsername());
    }

    public User toUser(NewUserRequest userRequest){
        return new User()
                .setEmail(userRequest.getEmail().toLowerCase())
                .setName(userRequest.getName())
                .setEnabled(true)
                .setRole(userRequest.getRole());
    }
}
