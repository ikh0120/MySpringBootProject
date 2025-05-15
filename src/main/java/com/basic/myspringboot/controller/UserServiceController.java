package com.basic.myspringboot.controller;

import com.basic.myspringboot.controller.dto.UserDTO;
import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserServiceController {
    private final UserService userService;

    @PostMapping
    public UserDTO.UserResponse create(@Valid @RequestBody //UserDTO.UserCreateRequest의 @NotBlank가 동작하려면 @Valid를 넣어줘야 함
                                           UserDTO.UserCreateRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User savedUser = userService.createUser(user);
        return new UserDTO.UserResponse(savedUser);
    }
}
