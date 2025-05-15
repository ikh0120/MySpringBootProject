package com.basic.myspringboot.controller;

import com.basic.myspringboot.controller.dto.UserDTO;
import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usrs")
public class UserServiceController {
    private final UserService userService;

    @PostMapping
    public UserDTO.UserResponse create(@RequestBody UserDTO.UserCreateRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getName());

        User savedUser = userService.createUser(user);
        return new UserDTO.UserResponse(savedUser);
    }



}
