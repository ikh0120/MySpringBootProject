package com.basic.myspringboot.auth.service;

import com.basic.myspringboot.auth.model.UserInfo;
import com.basic.myspringboot.auth.model.UserInfoRepository;
import com.basic.myspringboot.auth.model.UserInfoUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**Spring Security의 동작 과정
 * 1. 클라이언트 요청
 *      사용자가 Form으로 /login 또는 인증이 필요한 API에 username과 password를 포함한 HTTP Servlet Request를 보냄
 *
 * 2. UsernamePasswordAuthenticationFilter 동작 => 여기까지는 Spring Security 기본 제공 필터가 처리
 *      - 이 필터가 요청을 가로채서 UsernamePasswordAuthenticationToken을 생성
 *      - 이 토큰에는 사용자의 입력값인 username, password가 들어감
 *      - 해당 토큰은 AuthenticationManager(interface)로 전달
 *
 * 3. AuthenticationManager(interface) -> 실제 구현체는 ProviderManager
 *    -> ProviderManager는 내부적으로 등록된 AuthenticationProvider 들을 순회하면서 Token 체크 후 인증을 시도
 *
 * 4. UserDetailsService(interface)를 구현한 구현체 UserInfoUserDetailsService (: 사용자 정의 클래스, Spring Security가 아님)
 *      -> 이 클래스는 DB에서 username 기반으로 사용자 정보를 로드하는 역할
 *
 *      => UserInfoUserDetailsService에서 JPA를 사용하려면:
 *           - 엔티티 클래스인 UserInfo와 JPA 인터페이스인 UserInfoRepository를 만들어야 함
 *           - UserInfo는 DB에 저장된 회원 정보 (ex: username, password, roles 등)를 갖고 있는 Entity
 *
 * 5. UserDetails(interface)를 구현한 구현체: UserInfoUserDetails
 *      - 이 클래스는 Spring Security가 요구하는 인증 정보 구조를 따르기 위해 사용됨
 *      - 이 안에 UserInfo 엔티티의 값을 담아 Spring Security 내부에서 사용할 수 있도록 매핑함
 *      - User 엔티티는 따로 없음. Spring Security 자체에서 제공하는 `org.springframework.security.core.userdetails.User` 클래스를 사용할 수도 있고,
 *         직접 구현해서 UserInfoUserDetails를 사용할 수도 있음
 *
 * 6. 역순으로 되돌아가며
 *      UserDetails -> UserDetailsService -> AuthenticationProvider -> AuthenticationManager 순서로 인증 정보가 전달됨
 *
 * 7. AuthenticationManager(interface)를 구현한 ProviderManager에 인증 결과가 전달되고,
 *      여기서 최종적으로 인증 처리를 완료함 (비밀번호 비교 포함)
 *      => 비밀번호 비교는 AuthenticationProvider 내부에서 PasswordEncoder.matches(raw, encoded) 로 수행됨
 *
 * 8. 인증이 되지 않으면 Exception 발생 (ex: UsernameNotFoundException, BadCredentialsException 등)
 *      인증이 성공하면
 *      - UsernamePasswordAuthenticationToken 객체가 인증된 상태로 새로 생성됨
 *      - 이 객체는 SecurityContextHolder.getContext().setAuthentication() 을 통해 SecurityContext에 저장됨
 *
 * 9. 이후 HttpServletRequest(요청)를 받으면 Spring Security의 FilterChain이 실행되고,
 *      이미 인증된 사용자의 Authentication 객체가 SecurityContext에 존재하면
 *      인증 없이 바로 컨트롤러 등으로 요청이 전달됨
 *      => 만약 Authentication이 없거나 잘못되었으면, 인증 과정을 반복함
 */
/** 개념
 * UserDetailsService: Spring Security에서 사용자 정보를 DB에서 가져오는 인터페이스임
 * 로그인 시 username(email)을 받아 DB에서 사용자 정보를 찾아오는 역할
 * PasswordEncoder는 SecurityConfig에서 new BCryptPasswordEncoder()를 반환값으로 가져 BCrypt 단방향 암호화 알고리즘을 적용하는 것을 알 수 있음
 */
@Service //서비스(비즈니스 로직) 컴포넌트
public class UserInfoUserDetailsService implements UserDetailsService { //Spring Security가 요구하는 UserDetailsService 인터페이스 구현체
    @Autowired //DB에서 사용자 정보를 조회하기 위해 UserInfoRepository를 주입받음
    private UserInfoRepository repository;

    @Autowired //비밀번호 암호화를 위해 SecurityConfig에서 주입받음
    private PasswordEncoder passwordEncoder;

    @Override //로그인 요청 시 입력한 username(email)을 인자로 받아 DB에서 사용자 정보를 조회하는 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> optionalUserInfo = repository.findByEmail(username);
                //조회 된 UserInfo 엔티티를 Spring Security가 이해할 수 있는 UserDetails 타입으로 변환
        return optionalUserInfo.map(userInfo -> new UserInfoUserDetails(userInfo)) //userInfo.map(UserInfoUserDetails::new)
                // 입력한 username과 매칭되는 엔티티가 없다면 예외를 발생시켜 인증 실패를 알림
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }

    //회원가입 시 호출, 평문 비밀번호를 BCrypt로 단방향 암호화를 진행 한 뒤 DB에 저장함
    public String addUser(UserInfo userInfo) {
        //패스워드를 암호화 함
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        //UserInfo 엔티티에 username과 password를 DB에 저장
        UserInfo savedUserInfo = repository.save(userInfo);
        return savedUserInfo.getName() + " user added!!";
    }
}