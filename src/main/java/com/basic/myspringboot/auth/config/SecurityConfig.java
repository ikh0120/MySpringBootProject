package com.basic.myspringboot.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration //스프링 설정 클리스임을 명시
@EnableWebSecurity //웹 시큐리티를 활성화시키겠다
//400: 입력 오류, 401: 인증 실패, 403: 권한 없음
//403 error가 아니고 500 error인 이유는 DefaultExceptionAdvice의 handleException()의 HttpStatus.INTERNAL_SERVER_ERROR 때문이다.
@EnableMethodSecurity //권한 체크
public class SecurityConfig {

    /**BCrypt 알고리즘은 평문으로 문자열을 입력받고 단방향 암호화를 적용해서 해시 문자열로 반환한다.
     * 해시코드란?
     *  ex) $2a$10$8A94EhX6nWaE1ToqVrxLHuX.w0zQmPIUncwOLqpxaE4G5hxV0eF3q
     * 60자리 암호화 문자열로
     * $:       구분자
     * 2a:      2자리는 BCrypt 알고리즘 버전
     * 10:      2자리는 반복횟수(2^10 번 해싱 반복 수행)
     * Salt:    $ 구분자 뒤 22자리가 Salt값이다 -> 랜덤한 값으로 해시 결과에 영향을 주며 같은 평문이어도 다른 해시값을 만들도록 함
     * Hash:    Salt값 뒤 나머지 31자리 문자열이 입력 된 평문 문자열을 암호화 한 해시 값이다
     */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**CSRF 공격이란
     * CSRF(Cross-Site Request Forgery): 해커가 사용자 권한을 도용해 원하지 않는 요청을 보내는 공격이다
     *   CSRF 공격 시나리오
     *      1) 내가 A라는 은행 웹사이트에 로그인 한 상태라 가정함
     *      2) 공격자가 나에게 악성 코드가 포함된 사이트(B)의 링크를 클릭하게 유도
     *      3) B사이트에 방문하면, 자동으로 A 사이트에 요청 전송(ex: "계좌이체 요청")
     *      4) 쿠키는 브라우저에 자동 포함되므로, 정상 사용자 요청처럼 보임
     *      5) 서버는 인증된 요청으로 오해하고 처리함
     *   CSRF 방지 방법
     *      1. CSRF 토큰 사용(Spring Security 기본 제공)
     *         - 서버는 매 요청마다 고유한 토큰을 HTML form에 포함
     *         - 요청 시 이 토큰이 함께 오지 않으면 거부
     *      2. SameSite 쿠키 설정
     *         - 브라우저가 외부 사이트에서 쿠키 전송을 제한함
     *      3. Referer 체크 (요청 출처 검사)
     *   Spring Security에서의 CSRF
     *      - 기본적으로 CSRF 방지가 활성화 되어 있음
     *      - API나 비로그인 서비스에선 보통 .csrf().disable()을 사용해서 끄는 경우도 있음
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) //csrf 공격 방어 메서드를 비활성화 시키겠다 /**실전에서 절대 이러면 안됨*/
                .authorizeHttpRequests(auth -> {
                    //"/api/users/welcome"패스는 인증 없이 접근 가능한 경로이다
                    auth.requestMatchers("/api/users/welcome").permitAll()
                            // "/api/users/**"패스는 인증 후에만 접근 가능한 경로이다.
                            // postman에서 get 요청을 보낼 시 html 마크업 코드가 출력됨 해당 코드는 Username, Password를 입력받고 Submit 버튼이 존재함
                            .requestMatchers("/api/users/**").authenticated();
                })
                //스프링이 제공하는 인증 Form을 사용하겠다
                .formLogin(withDefaults())
                .build();
    }

    @Bean
    //Authentication
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
        UserDetails admin = User.withUsername("adminboot")
                .password(encoder.encode("pwd1"))
                .roles("ADMIN")  //관리자
                .build();
        UserDetails user = User.withUsername("userboot")
                .password((encoder.encode("pwd2")))
                .roles("USER")  //일반 사용자
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }
}
