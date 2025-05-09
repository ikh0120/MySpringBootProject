package com.basic.myspringboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//DB에 Customer라는 테이블을 만들어 줌
//MyBatis의 VO(Value Object) 클래스와 유사
@Entity
@Table(name = "customers")//테이블명: customers // 이 어노테이션이 없으면 테이블명을 클래스 이름으로 만들어줌
@Getter @Setter
public class Customer {
    //Primary Key, PK 값을 persistence provider(구현체인 Hibernate) 가 알아서 결정해라
//    @Id @GeneratedValue(strategy = GenerationType.AUTO) //Auto로 할 경우 테이블명_seq 테이블이 생성됨
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // Maria DB라 IDENTITY로 할것이다
    private Long id;

    //중복을 허용하지 않고, null 값을 허용하지 않음
    @Column(unique = true, nullable = false)
    private String customerId;

    //null 값을 허용하지 않음
    @Column(nullable = false)
    private String customerName;


}
