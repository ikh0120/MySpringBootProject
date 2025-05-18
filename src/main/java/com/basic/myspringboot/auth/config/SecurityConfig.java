package com.basic.myspringboot.auth.config;

import com.basic.myspringboot.auth.service.UserInfoUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * 개념
 * Spring Security의 설정 클래스
 * Http 보안 정책, 로그인 방식, 사용자 인증 로직 설정
 * PasswordEncoder 빈 등록
 * 인메모리 사용자 등록(샘플용)
 */
@Configuration //스프링 설정 클래스임을 나타냄
@EnableWebSecurity //Spring Security의 웹 보안을 활성화
@EnableMethodSecurity //메서드 단위 권한 검사를 활성화
public class SecurityConfig {

    /**
     * BCrypt 알고리즘은 평문으로 문자열을 입력받고 단방향 암호화를 적용해서 해시 문자열로 반환한다.
     * 해시코드란?
     * ex) $2a$10$8A94EhX6nWaE1ToqVrxLHuX.w0zQmPIUncwOLqpxaE4G5hxV0eF3q
     * 60자리 암호화 문자열로
     * $:       구분자
     * 2a:      2자리는 BCrypt 알고리즘 버전
     * 10:      2자리는 반복횟수(2^10 번 해싱 반복 수행)
     * Salt:    $ 구분자 뒤 22자리가 Salt값이다 -> 랜덤한 값으로 해시 결과에 영향을 주며 같은 평문이어도 다른 해시값을 만들도록 함
     * Hash:    Salt값 뒤 나머지 31자리 문자열이 입력 된 평문 문자열을 암호화 한 해시 값이다
     */

    @Bean //BCryptPasswordEncoder 객체를 빈으로 등록(패스워드 단방향 암호화)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CSRF 공격이란
     * CSRF(Cross-Site Request Forgery): 해커가 사용자 권한을 도용해 원하지 않는 요청을 보내는 공격이다
     * CSRF 공격 시나리오
     * 1) 내가 A라는 은행 웹사이트에 로그인 한 상태라 가정함
     * 2) 공격자가 나에게 악성 코드가 포함된 사이트(B)의 링크를 클릭하게 유도
     * 3) B사이트에 방문하면, 자동으로 A 사이트에 요청 전송(ex: "계좌이체 요청")
     * 4) 쿠키는 브라우저에 자동 포함되므로, 정상 사용자 요청처럼 보임
     * 5) 서버는 인증된 요청으로 오해하고 처리함
     * CSRF 방지 방법
     * 1. CSRF 토큰 사용(Spring Security 기본 제공)
     * - 서버는 매 요청마다 고유한 토큰을 HTML form에 포함
     * - 요청 시 이 토큰이 함께 오지 않으면 거부
     * 2. SameSite 쿠키 설정
     * - 브라우저가 외부 사이트에서 쿠키 전송을 제한함
     * 3. Referer 체크 (요청 출처 검사)
     * Spring Security에서의 CSRF
     * - 기본적으로 CSRF 방지가 활성화 되어 있음
     * - API나 비로그인 서비스에선 보통 .csrf().disable()을 사용해서 끄는 경우도 있음
     */
    @Bean //Http 보안 필터 체인을 정의
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //csrf 공격 방어 메서드를 비활성화 시키겠다 /**실전에서 절대 이러면 안됨 지금은 설정이 빡쎄사 잠깐 이렇게 설정한 것임*/
        return http.csrf(csrf -> csrf.disable())
                //요청 별 접근 권한 설정
                .authorizeHttpRequests(auth -> {
                    // 해당 경로들은 인정없이 접근 허용
                    auth.requestMatchers("/api/users/welcome", "/userinfos/new").permitAll()
                            //해당 경로는 인증 필요
                            .requestMatchers("/api/users/**").authenticated();
                })
                //스프링이 제공하는 기본 로그인 Form을 사용하겠다
                .formLogin(withDefaults())
                .build();
    }

    //개발자 계정 정보 => username: admin@aa.com   password: pwd1
    //사용자 계정 정보 => username: user@aa.com   password: pwd2
    //개발자가 커스텀한 UserServiceDetail 서비스를 SpringBean으로 등록하기
    @Bean
    public UserDetailsService userDetailsService() {
        //우리가 UserDetailsService를 구현해서 만들었다고 반환하기
        return new UserInfoUserDetailsService();
    }

    @Bean /**AuthenticationProvider에 DaoAuthenticationProvider()를 사용함*/
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //UserServiceDetail 서비스가 어떤 객체인지 DaoAuthenticationProvider()에 알려주기
        authenticationProvider.setUserDetailsService(userDetailsService());
        //패스워드 인코더가 어떤 객체인지 DaoAuthenticationProvider()에 알려주기
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
//    @Bean
//    //Authentication
//    //인메모리 사용자 저장소 빈 등록(실전에서 DB 기반으로 바꿔야 함)
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//        //관리자 계정 생성         //admin.username = "adminboot";
//        UserDetails admin = User.withUsername("adminboot")  // UserDetails => User.UserBuilder
//                //admin.password = encoder.encode("pwd1");
//                .password(encoder.encode("pwd1"))
//                //admin.roles = "ADMIN"; => admin.roles = "ROLE_ADMIN";
//                .roles("ADMIN")
//                .build();   // User.UserBuilder => UserDetails
//        //일반 사용자 계정 생성      // user.username = "userboot";
//        UserDetails user = User.withUsername("userboot")  // UserDetails => User.UserBuilder
//                //user.password = (encoder.encode("pwd2"));
//                .password((encoder.encode("pwd2")))
//                //user.roles = "USER" => user.roles = "ROLE_USER"
//                .roles("USER")
//                .build();   // User.UserBuilder => UserDetails
//
//        //로그인 시 이러한 오류가 남
//        //No AuthenticationProvider found for org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//        //사용자 정보를 InMemoryUserDetailsManager에 저장함 -> 메모리에만 저장되며, DB에는 저장되지 않음
//        //InMemoryUserDetailsManager는 주로 테스트나 데모용으로 사용함
//        return new InMemoryUserDetailsManager(admin, user);
//    }

}
