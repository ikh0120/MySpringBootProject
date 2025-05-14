package com.basic.myspringboot.controller;

import com.basic.myspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //@RestController(=@ResponseBody + @Controller)가 아닌 그냥 @Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/thymeleaf")
    public String leaf(Model model) { //Model 객체: Spring Framework에서 UI 상에서 쓸 데이터를 저장해주는 객체
        model.addAttribute("name", "스프링 부트!");
        return "leaf";
    }
}
