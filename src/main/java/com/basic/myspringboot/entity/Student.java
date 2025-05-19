package com.basic.myspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

//Student
@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
//Non-Owner(종속) - 양방향관계: DB 테이블에는 나타나지 않고 외래키(FK)만 설정된다
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String studentNumber;

    //@OneToOne 어노테이션 설정을 하지 않으면 StudentDetail의 변수값을 보지 못함
    //mappedBy = "student" => 참조하는 키의 변수명, CascadeType.PERSIST: 등록, CascadeType.REMOVE: 삭제
    //1:1 지연로딩 //양방향 Student에서 StudentDetail을 참조할 수 있도록 FK에 해당하는 필드명 mappedBy에 설정한다
    //Student와 StudentDetail 의 라이프사이클이 동일하다. //mappedBy가 있다면 연관관계의 주인이 아님을 표시
    // mappedBy가 있는 쪽은 읽기 전용임
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private StudentDetail studentDetail;
}