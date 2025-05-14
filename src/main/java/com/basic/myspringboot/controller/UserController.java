package com.basic.myspringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //@RestController(=@ResponseBody + @Controller)가 아닌 그냥 @Controller
public class UserController {
    @GetMapping("/thymeleaf")
    public String leaf(Model model) { //Model 객체: Spring Framework에서 UI 상에서 쓸 데이터를 저장해주는 객체
        model.addAttribute("name", "스프링 부트!");
        return "leaf";
    }
}
