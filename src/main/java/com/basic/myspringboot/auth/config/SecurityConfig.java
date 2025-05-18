package com.basic.myspringboot.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration //스프링 설정 클리스임을 명시
@EnableWebSecurity //웹 시큐리티를 활성화시키겠다
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

    @Bean
    //authentication
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
        UserDetails admin = User.withUsername("adminboot")
                .password(encoder.encode("pwd1"))
                .roles("ADMIN")
                .build();
        UserDetails user = User.withUsername("userboot")
                .password((encoder.encode("pw")))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }

}
