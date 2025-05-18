package com.basic.myspringboot.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//해당 클래스는 Spring Security의 UserDetails<<interface>>의 구현체이고, UserDetails를 상속받은 User Entity에 값을 저장함
//UserInfoUserDetailsService -- UserInfoUserDetails
public class UserInfoUserDetails implements UserDetails {

    private String email;
    private String password;
    private List<GrantedAuthority> authorities;
    private UserInfo userInfo;

    public UserInfoUserDetails(UserInfo userInfo) {
        this.userInfo = userInfo;
        //UserInfo 엔티티의 이메일 주소를 username 변수에 저장
        this.email=userInfo.getEmail();
        //UserInfo 엔티티의 패스워드를 password 변수에 저장
        this.password=userInfo.getPassword();
        this.authorities= Arrays.stream(userInfo.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    //Spring Security의 AuthenticationManager를 상속받은 ProviderManager가 인증 처리할 때 사용함
    @Override
    public String getPassword() {
        return password;
    }

    //Spring Security의 AuthenticationManager를 상속받은 ProviderManager가 인증 처리할 때 사용함
    @Override
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