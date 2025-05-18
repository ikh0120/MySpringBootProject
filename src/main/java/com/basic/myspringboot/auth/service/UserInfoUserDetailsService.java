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
@Service
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //로그인 시 사용자가 입력 한 username을 인자로 전달받는다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> optionalUserInfo = repository.findByEmail(username);
                //입력한 username과 매칭되는 엔티티가 존재한다면 UserInfo 객체를 UserDetail의 구현체인 UserInfoUserDetails 객체로 전달한다
        return optionalUserInfo.map(userInfo -> new UserInfoUserDetails(userInfo)) //userInfo.map(UserInfoUserDetails::new)
                // 입력한 username과 매칭되는 엔티티가 없다면 인증 오류
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

    }

    public String addUser(UserInfo userInfo) {
        //패스워드를 인코딩해서 저장
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        //UserInfo 엔티티에 username과 password를 DB에 저장
        UserInfo savedUserInfo = repository.save(userInfo);
        return savedUserInfo.getName() + " user added!!";
    }
}