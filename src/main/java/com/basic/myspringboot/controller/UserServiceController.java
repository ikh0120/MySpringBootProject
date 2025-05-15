package com.basic.myspringboot.controller;

import com.basic.myspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usrs")
public class UserServiceController {
    private final UserService userService;


}
