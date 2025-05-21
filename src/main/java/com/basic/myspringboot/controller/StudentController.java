package com.basic.myspringboot.controller;

import com.basic.myspringboot.controller.dto.StudentDTO;
import com.basic.myspringboot.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//StudentController 클래스
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentDTO.Response>> getAllStudents() {
        List<StudentDTO.Response> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    //JPQL - Fetch Join을 사용해서 하나의 쿼리문만 나오는데
    //Hibernate:
        // select s1_0.student_id,s1_0.name,
        //	sd1_0.student_detail_id,sd1_0.address,sd1_0.date_of_birth,sd1_0.email,sd1_0.phone_number,
        //	s1_0.student_number
        //from students s1_0 join student_details sd1_0 on s1_0.student_id=sd1_0.student_id
        //where s1_0.student_id=?
        //
    //Native Query
        //select s.name, s.student_number, sd.address, sd.email
        //from students s, student_details sd
        //where s.student_id = sd.student_id;
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO.Response> getStudentById(@PathVariable Long id) {
        StudentDTO.Response student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    //JPQL - Fetch Join을 사용하지 않아 Query Method 방식으로 출력됨
    //Hibernate:
        // select s1_0.student_id,s1_0.name,s1_0.student_number
        // from students s1_0
        // where s1_0.student_number=?
    //Hibernate:
        // select sd1_0.student_detail_id,sd1_0.address,sd1_0.date_of_birth,
        //  sd1_0.email,sd1_0.phone_number,sd1_0.student_id
        // from student_details sd1_0
        // where sd1_0.student_id=?
    @GetMapping("/number/{studentNumber}")
    public ResponseEntity<StudentDTO.Response> getStudentByStudentNumber(@PathVariable String studentNumber) {
        StudentDTO.Response student = studentService.getStudentByStudentNumber(studentNumber);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<StudentDTO.Response> createStudent(@Valid @RequestBody StudentDTO.Request request) {
        StudentDTO.Response createdStudent = studentService.createStudent(request);
        //HttpStatus.CREATED - 201
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    //JPQL(address, email 수정)
    //Hibernate:
    //	select s1_0.student_id,s1_0.name,s1_0.student_number
    //	from students s1_0
    //	where s1_0.student_id=?
    //Hibernate:
    //	select sd1_0.student_detail_id,sd1_0.address,sd1_0.date_of_birth,
    //		sd1_0.email,sd1_0.phone_number,sd1_0.student_id
    //	from student_details sd1_0
    //	where sd1_0.student_id=?
    //Hibernate:
    //	select sd1_0.student_detail_id
    //	from student_details sd1_0
    //	where sd1_0.email=? limit ?
    //Hibernate:
    //	update student_details
    //	set address=?,date_of_birth=?,email=?,phone_number=?,student_id=?
    //	where student_detail_id=?
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO.Response> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentDTO.Request request) {
        StudentDTO.Response updatedStudent = studentService.updateStudent(id, request);
        return ResponseEntity.ok(updatedStudent);
    }

    //JPQL
    //Hibernate:    //갯수조회
    //	select count(*)
    //	from students s1_0
    //	where s1_0.student_id=?
    //Hibernate:    //students 테이블 조회
    //	select s1_0.student_id,s1_0.name,s1_0.student_number
    //	from students s1_0
    //	where s1_0.student_id=?
    //Hibernate:    //student_details 테입르 조회
    //	select sd1_0.student_detail_id,sd1_0.address,sd1_0.date_of_birth,
    //		sd1_0.email,sd1_0.phone_number,sd1_0.student_id
    //	from student_details sd1_0
    //	where sd1_0.student_id=?
    //Hibernate:    //student_details부터 삭제하고
    //	delete
    //	from student_details
    //	where student_detail_id=?
    //Hibernate:    //student 삭제
    //	delete
    //	from students
    //	where student_id=?
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
