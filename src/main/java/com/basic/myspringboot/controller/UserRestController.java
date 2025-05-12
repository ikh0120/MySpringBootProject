package com.basic.myspringboot.controller;

import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
//"/api/users"라는 요청이 URL로 들어오면 UserRestController가 동작함
//여기서 @GetMapping("/api/users")를 넣으면 앞에 @RequestMapping에 넣은 값이 들어와서 @GetMapping("/api/users/api/users")로 들어옴
@RequestMapping("/api/users")
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
    /**
     * Postman 사용법 - POST 요청
     *
     * 1. 요청 방식(Method)을 POST로 설정
     * 2. 주소창에 http://localhost:8080/api/users 입력
     * 3. Headers 탭에서 다음 설정 추가:
     *    - Key: Content-Type
     *    - Value: application/json
     * 4. Body 탭에서:
     *    - raw 라디오 버튼 선택
     *    - 우측 드롭다운에서 JSON 선택
     *    - 아래와 같은 JSON 데이터 입력:
     *      {
     *        "name": "스프링",
     *        "email": "spring@a.com"
     *      }
     * 5. Send 버튼 클릭
     *
     * 응답 결과:
     * {
     *     "id": 1,
     *     "name": "스프링",
     *     "email": "spring@a.com",
     *     "createdAt": "2025-05-12T07:29:34.523809"
     * }
     *
     * 이 데이터는 users 테이블에 자동으로 저장됨
     */

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    /**
     * Postman 사용법 - GET 요청
     *
     * 1. 요청 방식(Method)을 GET으로 설정
     * 2. 주소창에 http://localhost:8080/api/users 입력
     * 3. Send 버튼 클릭
     *
     * 응답 결과 :
     * [
     *     {
     *         "id": 1,
     *         "name": "스프링",
     *         "email": "spring@a.com",
     *         "createdAt": "2025-05-12T07:29:34.523809"
     *     },
     *     {
     *         "id": 2,
     *         "name": "스프링2",
     *         "email": "spring2@a.com",
     *         "createdAt": "2025-05-12T08:13:32.63707"
     *     }
     * ]
     *
     * - `[]`는 전체 결과를 나타내는 리스트(List)
     * - `{}`는 각각의 사용자(User) 객체를 나타냄
     * - DB에 저장된 User가 많아지면 리스트 안에 여러 개의 User 객체가 포함됨
     */

    /**
     * GET /api/users/{id} 요청 처리
     * @PathVariable: URL 경로에서 {id} 값을 받아 매개변수로 바인딩
     */
    /**
     * ResponseEntity:
     *      Body + Http Status Code + Response Headers까지 한번에 담아서 응답을 주는 객체
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> optionalUser = userRepository.findById(id);
/*
        // public <U> Optional<U> map(Function<? super T, ? extends U> mapper)
        // Function의 추상메서드 R apply(T t)
        // orElse(): Optional 객체 안의 값이 없으면 이걸 반환해라
 */
        /**1. 가장 기본*/
//        ResponseEntity<User> responseEntity = optionalUser
//                .map(user -> ResponseEntity.ok(user)) //optionalUser에 User 객체가 들어있는 경우: status code = 200
//                    .orElse(ResponseEntity.notFound().build()); //optionalUser에 User객체가 없는 경우: status code = 404
//        return responseEntity;

        /**
         * responseEntity의 return값
         *
         * if(optionalUser.isPresent()==True) { //Optinal<User>안에 User 객체가 존재할 때
         *      return ResponseEntity<User>, HTTP.status_code=200 OK, body=optionalUser.get();
         * }
         * else {
         *      return ResponseEntity<User>, HTTP.statusCode = 404 Not Found, body = null;
         * }
         */
        /**2. 가장 짧은 버전*/
        /**가장 lambda 식을 많이 사용한 버전*/
//        return optionalUser.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());


        /**3. optionalUser의 HttpStatus가 404일 때 body에 단순한 메세지("User Not Found") 추가*/
        ResponseEntity<User> responseEntity = optionalUser
                .map(user -> ResponseEntity.ok(user)) //optionalUser에 User 객체가 들어있는 경우: status code = 200
                    //.orElse(ResponseEntity.notFound().build()); //optionalUser에 User객체가 없는 경우: status code = 404
                    .orElse(new ResponseEntity("User Not Found", HttpStatus.NOT_FOUND));
        return responseEntity;
    }
}
