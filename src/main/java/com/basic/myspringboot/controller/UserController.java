package com.basic.myspringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/thymeleaf")
    public String leaf(Model model) {
        model.addAttribute("name", "basic");
        return "leaf";
    }
}
