package com.basic.myspringboot.controller;

import com.basic.myspringboot.repository.UserRepository;
import org.springframework.web.bind.annotation.RestController;

//@RestController = @Controller + @ResponseBody
@RestController
public class UserRestController {
    private UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        System.out.println(">>> UserController: "+userRepository.getClass().getName());
        this.userRepository = userRepository;
    }
}
