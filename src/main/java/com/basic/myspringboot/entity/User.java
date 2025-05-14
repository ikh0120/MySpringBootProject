package com.basic.myspringboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/** 테이블은 @SpringBootApplication이 붙은 메인 메서드를 실행시켜야지만 자동으로 생성된다*/
/**
 * 객체가 생성될 때
 * ID는 자동으로 증가하는 기본키이고 createdAt은 @CreationTimestamp로
 * 현재 시간을 넣도록 설정한 것이어서
 * 우리가 넣어야 하는 값은 name과 email 두개가 다임
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Name은 필수 입력 항목입니다. ")
    private String name;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email은 필수 입력 항목입니다. ")
    private String email;

    // 자동으로 User 객체가 생성될 때 현재 시간을 넣도록 설정
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

}