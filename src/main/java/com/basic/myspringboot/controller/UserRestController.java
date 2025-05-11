package com.basic.myspringboot.controller;

import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.repository.CustomerRepository;
import com.basic.myspringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * HTTP POST 요청을 처리하는 메서드
 *
 * - 클라이언트(Postman 등)로부터 JSON 형식의 사용자 데이터를 수신함
 * - @RequestBody 어노테이션을 통해 JSON을 User 객체로 변환함 (역직렬화)
 * - 변환된 User 객체를 JPA의 userRepository.save()로 DB에 저장
 * - 저장된 User 객체를 응답으로 반환 (ID 및 생성 시간 등 포함)
 *
 * 예시:
 * POST http://localhost:8080/api/users
 * {
 *   "name": "스프링",
 *   "email": "spring@a.com"
 * }
 *
 * 응답:
 * {
 *   "id": 1,
 *   "name": "스프링",
 *   "email": "spring@a.com",
 *   "createdAt": "2025-05-12T07:29:34.523809"
 * }
 */

//@RestController = @Controller + @ResponseBody
@RestController
@RequiredArgsConstructor //final인 변수를 초기화하는 생성자를 자동으로 생성해주는 역할을 하는 lombok 어노테이션
@RequestMapping("/api/users") //"/api/users"라는 요청이 URL로 들어오면 UserRestController가 동작함
public class UserRestController {
    /**
     * Constructor Injection & Lombok 자동 생성자 주입 설명
     * final로 선언한 필드는 반드시 생성자에서 초기화해줘야 함
     *  원래는 생성자 주입(Setter Injection)방식인
     *      @Autowired와 @Qualifier("userRepository")
     *      또는
     *      @Resource(name = "userRepository")를 사용해서 의존성을 주입할 수 있음
     * 하지만 지금은 생성자 주입(Constructor Injection) 방식을 사용하여 의존성을 주입함
     * 이 방식은 final 필드를 안정적으로 초기화할 수 있고, 테스트에도 유리함
     *      다만, 의존성이 많아질 경우 생성자를 일일이 작성하는 것이 번거롭기 때문에,
     *      lombok의 @RequiredArgsConstructor를 사용하면 자동으로 생성자가 만들어져 생성자 주입을 간편하게 사용할 수 있음
     */
    private final UserRepository userRepository;

    //<Alt> + <Insert> 누르면 생성자를 만들 수 있음
//    //Constructor Injection(생성자 주입)
//    public UserRestController(UserRepository userRepository) {
//        System.out.println(">>> UserController: "+userRepository.getClass().getName()); // >>> UserController: jdk.proxy4.$Proxy153
//        this.userRepository = userRepository;
//    }

    /** Mockito 라이브러리 설명
     * Mock 객체란 테스트 케이스에서 실제 객체를 대체하는 가짜 객체
     * A가 Controller를 만들고 B가 Repository를 만드는데
     * B의 구현이 아직 완료되지 않았거나, 실제 DB를 사용하고 싶지 않을 때,
     * 가짜로 동작하는 Mock 객체를 주입받아 쓸 수 있음
     *
     * java의 Mockito 라이브러리를 사용해서 할 수 있음
     * 하지만 @Autowired를 사용해서 필드 주입을 사용하게 되면
     * Mockito가 생성한 Mock 객체를 주입하는게 어려움
     * 그래서 테스트 코드에서는 보통 생성자 주입(Constructor Injection)을 사용함
     * 이는 테스트 시 Mock 객체를 명확하게 주입하기 더 적합하고 테스트 코드가 더 유연해짐
     */

    //Request Mapping 중 하나인 PostMapping
    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }
    /**Postman 사용법
    Post Man에서 POST로 드롭박스를 바꾸고 텍스트필드에 http://localhost:8080/api/users 입력 후
    Headers 메뉴에서 Key를 Content-Type으로 Value를 application/json으로 설정
    Body 메뉴에서 라디오버튼 중 raw 선택 후 JSON으로 변경(Headers 메뉴에서 Content-Type을 application/json으로 설정해서 JSON이 기본 값)후
    {
     "name":"스프링",
     "email":"spring@a.com"
    }
    라는 JSON 데이터를 넣어주고 http://localhost:8080/api/users를 넣은 텍스트필드의 오른쪽에 있는 Send 버튼을 클릭하면
    밑의 Body 메뉴에
    {
        "id": 1,
        "name": "스프링",
        "email": "spring@a.com",
        "createdAt": "2025-05-12T07:29:34.523809"
    }
    가 들어가면서 자동으로 내 users 테이블에 값 저장됨
    */

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
