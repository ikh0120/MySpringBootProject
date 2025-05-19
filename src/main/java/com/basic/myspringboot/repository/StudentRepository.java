package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//StudentRepository 인터페이스
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentNumber(String studentNumber);

    /**Fetch Join이란?
     *      Fetch Join은 JPA에서 성능 최적화를 위해 제공하는 특별한 JOIN 기능으로, 연관된 엔티티나 컬렉션을 한 번의 SQL 쿼리로 함께 조회하는 기능입니다.
     *   주요 기능
     *      1. 즉시 로딩 최적화: Fetch Join을 사용하면 지연 로딩(Lazy Loading)으로 설정된 연관 관계도 한 번에 조회 가능
     *      2. N+1 문제 해결: 연관된 엔티티를 조회할 때 발생하는 N+1 쿼리 문제를 방지
     *      3. 단일 쿼리 실행: 여러 엔티티를 조인하여 하나의 쿼리로 가져옴
     *   필요성
     *      1. 성능 최적화:
     *          - 일반적인 지연 로딩은 처음 엔티티 조회 시 1번, 연관 엔티티 접근 시 N번의 쿼리가 발생(N+1 문제)
     *          - Fetch Join은 처음부터 연관 데이터를 함께 가져오므로 추가 쿼리 발생 없음
     *      2. 불필요한 쿼리 감소:
     *      3. 컬렉션 조회 시 유용: 일대다 관계의 컬렉션을 조회할 때 특히 효과적
     *   주의사항
     *      1. 페이징 문제: 컬렉션 Fetch Join에 페이징 적용 시 메모리에서 처리되므로 주의 필요
     *      2. 중복 데이터: 일대다 조인 시 결과에 중복이 발생할 수 있어 DISTINCT 사용 필요
     *      3. 성능 고려: 불필요한 Fetch Join은 오히려 성능 저하를 초래할 수 있음
     */
    @Query("SELECT s FROM Student s JOIN FETCH s.studentDetail WHERE s.id = :id")
    Optional<Student> findByIdWithStudentDetail(@Param("id") Long id);
    
    boolean existsByStudentNumber(String studentNumber);
}
