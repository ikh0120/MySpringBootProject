package com.basic.myspringboot.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//해당 클래스는 Spring Security의 UserDetailsService(interface)를 구현하기 위해
//UserInfoUserDetailsService를 만들어야 하는데
//JPA를 사용하기에 Service 클래스는 기본적으로
//Entity(UserInfo), Repository(UserInfoRepository)가 우선적으로 구현되어야 하기에 먼저 만들어 진 클래스임
@Entity
@Data
@AllArgsConstructor //모든 필드를 받는 생성자 자동 생성
@NoArgsConstructor  //기본 생성자 자동 생성
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false) //email 컬럼으로 인증할거라 unique = true 속성 넣음
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    //Admin, User
    private String roles;
}