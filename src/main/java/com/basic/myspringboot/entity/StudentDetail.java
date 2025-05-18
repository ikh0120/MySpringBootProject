package com.basic.myspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

//StudentDetail 클래스
@Entity
@Table(name = "student_details")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
//Owner(주인) - FK(외래키)를 가진 쪽이 주인임
public class StudentDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_detail_id")
    private Long id;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column
    private String email;
    
    @Column
    private LocalDate dateOfBirth;

    /** JPA(Java Persistence API)의 FetchType.LAZY와 FetchType.EAGER는
     ** 연관 관계에 있는 엔티티를 언제 데이터베이스에서 로드할지를 결정하는 전략
     *
     ** FetchType.LAZY(지연 로딩) - 이게 최선
     *  - 연관된 엔티티를 실제로 사용할 때 까지 로딩을 지연시킴
     *  - 프록시 객체를 생성하여 실제 데이터가 필요한 순간에 DB 쿼리를 실행
     *  - 메모리 효율적이고 초기 로딩 시간이 빠름
     *
     ** FetchType.EAGER(즉시 로딩) - 성능에 좋지 않음
     *  - 엔티티를 로딩할 때 연관된 엔티티도 함께 즉시 로딩
     *  - 한 번의 쿼리로 모든 연관 데이터를 가져옴(Join 활용)
     *  - 즉시 모든 데이터를 메모리에 로드하므로 메모리 사용량이 높을 수 있음
     */
    //1:1 지연 로딩
    @OneToOne(fetch = FetchType.LAZY) //Default: fetch = FetchType.EAGER
    //@JoinColumn은 FK(외래키)에 해당하는 어노테이션 //Owner 쪽에 있어야 함
    @JoinColumn(name = "student_id", unique = true) //Student 엔티티의 PK가 들어감
    private Student student;
}