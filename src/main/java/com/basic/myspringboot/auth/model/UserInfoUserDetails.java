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
        this.email=userInfo.getEmail(); //UserInfo entity에서 가져온 email을 멤버변수에 저장
        this.password=userInfo.getPassword(); //UserInfo entity에서 가져온 password을 멤버변수에 저장
        this.authorities= Arrays.stream(userInfo.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password; //Spring Security의 AuthenticationManager를 상속받은 ProviderManager가 사용함
    }

    @Override
    public String getUsername() {
        return email; //Spring Security의 AuthenticationManager를 상속받은 ProviderManager가 사용함
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