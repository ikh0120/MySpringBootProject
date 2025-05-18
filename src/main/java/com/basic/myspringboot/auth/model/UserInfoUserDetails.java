package com.basic.myspringboot.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**Spring Security가 인증 시 내부적으로 사용하는 사용자 정보 객체(UserDetails 인터페이스) 구현*/
public class UserInfoUserDetails implements UserDetails {

    private String email;
    private String password;
    /** public interface GrantedAuthority { String getAuthority(); } */
    //권한 이름 반환 메서드를 가지고 있는 인터페이스임
    private List<GrantedAuthority> authorities;
    private UserInfo userInfo;

    //생성자에서 UserInfo 엔티티를 받아서 내부 필드를 초기화 시킴
    public UserInfoUserDetails(UserInfo userInfo) {
        this.userInfo = userInfo;

        //UserInfo 엔티티의 이메일을 username 변수에 저장
        this.email=userInfo.getEmail();

        //UserInfo 엔티티의 패스워드를 password 변수에 저장
        this.password=userInfo.getPassword();


        /**public class SimpleGrantedAuthority implements GrantedAuthority {
         *      private final String role;
         *
         *      public SimpleGrantedAuthority(String role) { this.role = role; }
         *
         *      @Override
         *      public String getAuthority() { return role; }
         * }
         */
        this.authorities= Arrays.stream(userInfo.getRoles().split(",")) //List<GrantedAuthority> => Stream<String>
                // => ["ROLE_ADMIN", "ROLE_USER"]
                //.map(SimpleGrantedAuthority::new)
                .map(roleName -> new SimpleGrantedAuthority(roleName)) //Stream<String> => Stream<SimpleGrantedAuthority>
                // => new SimpleGrantedAuthority("ROLE_ADMIN")
                // => new SimpleGrantedAuthority("ROLE_USER")
                .collect(Collectors.toList()); //Stream<SimpleGrantedAuthority> => List<SimpleGrantedAuthority>
                // => List.of(
                //      new SimpleGrantedAuthority("ROLE_ADMIN"),
                //      new SimpleGrantedAuthority("ROLE_USER"),
                //    };
    }

    @Override //인증 후 권한 리스트 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }



    @Override
    //Spring Security의 AuthenticationManager가 인증 처리할 때 사용함
    public String getPassword() {
        return password;
    }

    @Override
    //Spring Security의 AuthenticationManager가 인증 처리할 때 사용함
    public String getUsername() {
        return email;
    }
    
    public UserInfo getUserInfo() {
        return userInfo;
    }    

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}