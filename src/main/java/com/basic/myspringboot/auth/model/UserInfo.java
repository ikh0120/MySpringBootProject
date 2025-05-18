package com.basic.myspringboot.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//해당 클래스는 Spring Security의 UserDetailsService<<interface>> -- UserInfoUserDetailsService -- UserInfoRepository -- UserInfo 클래스임
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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