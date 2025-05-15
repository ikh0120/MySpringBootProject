package com.basic.myspringboot.controller;

import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; //BindingResult가 Errors의 하위 클래스임
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller //@RestController(=@ResponseBody + @Controller)가 아닌 그냥 @Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // 2) static/index.html에서 넘어옴
    @GetMapping("/index")
    public String index(Model model) {
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
    //@Valid: 유효성 검사(값을 잘 입력했냐 아니냐) 수행 //Validator 클래스가 내부에서 만들어 지고 호출하는 역할
    //Errors: 에러가 발생했을 때 에러 정보 저장(어떠한 항목에 Error가 생겼다, 사용자 지정 에러 메세지는 뭐다)
    public String addUser(@Valid @ModelAttribute("userForm") User user,
                          Errors result, Model model) {
        if (result.hasErrors()) { //에러 정보가 있다면 add-user.html로 이동
            return "add-user";
        }
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
        //return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id,
                                 Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id,
                             @Valid @ModelAttribute("user") User user,
                             BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }
        userRepository.save(user);
        return "redirect:/index";
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/index";
    }

    @GetMapping("/thymeleaf")
    public String leaf(Model model) { //Model 객체: Spring Framework에서 UI 상에서 쓸 데이터를 저장해주는 객체
        model.addAttribute("name", "스프링 부트!");
        return "leaf";
    }
}
