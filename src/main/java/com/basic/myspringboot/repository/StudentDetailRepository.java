package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.StudentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//StudentDetailRepository 인터페이스
@Repository
public interface StudentDetailRepository extends JpaRepository<StudentDetail, Long> {
    
    Optional<StudentDetail> findByStudentId(Long studentId);
    
    @Query("SELECT sd FROM StudentDetail sd JOIN FETCH sd.student WHERE sd.id = :id")
    Optional<StudentDetail> findByIdWithStudent(@Param("id") Long id);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    boolean existsByEmail(String email);
}