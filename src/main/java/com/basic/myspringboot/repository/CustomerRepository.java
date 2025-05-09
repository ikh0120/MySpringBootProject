package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// DAO(Data Access Object) 클래스와 같은 역할 수행
// <Entity, 엔티티 pk의 type>
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /* Query Method 정의하면 JPA가 메서드 명에 맞는 JPQL(Java Persistence Query Language)로 변환하여 실행하고,이 JPQL은 내부적으로 SQL로 변환되어 실제 데이터베이스에 접근하게 된다.*/

    // Optional: null값을 명시적으로 처리하기 워한 컨테이너 객체
    // Customer는 Entity 클래스 이름
    // JPQL: select c from Customer c where c.customerId=?;
    Optional<Customer> findByCustomerId(String id);

    //Contains: "%customerName%"이라는 뜻임
    //select c from Customer c where c.customerName like %:name%;
    List<Customer> findByCustomerNameContains(String name);

}
