package com.basic.myspringboot.controller;

import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller //@RestController(=@ResponseBody + @Controller)가 아닌 그냥 @Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // 2) static/index.html에서 넘어옴
    @GetMapping("/index")
    public String index(Model model){
        // UserRepository를 injection 받음
        // userRepository의 findAll()을 사용해서 가져온 값들을 users라는 키 값에 저장
        // ==> Key: users, Value: userRepository.findAll()
        model.addAttribute("users", userRepository.findAll());
        return "index"; // 3) templates/index.html로 이동
    }

    @GetMapping("/signup")
    public String showSignupForm(@ModelAttribute("userForm") User user) { //@ModelAttribute가 없다면 User 클래스에서 첫 글자만 소문자로 바꾸고 들어감
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid @ModelAttribute("userForm") User user,
                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
        //return "redirect:/index";
    }

    @GetMapping("/thymeleaf")
    public String leaf(Model model) { //Model 객체: Spring Framework에서 UI 상에서 쓸 데이터를 저장해주는 객체
        model.addAttribute("name", "스프링 부트!");
        return "leaf";
    }
}
