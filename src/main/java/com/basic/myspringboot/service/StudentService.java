//StudentService 클래스
package com.basic.myspringboot.service;

import com.basic.myspringboot.controller.dto.StudentDTO;
import com.basic.myspringboot.entity.Student;
import com.basic.myspringboot.entity.StudentDetail;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.StudentDetailRepository;
import com.basic.myspringboot.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentDetailRepository studentDetailRepository;

    public List<StudentDTO.Response> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public StudentDTO.Response getStudentById(Long id) {
        Student student = studentRepository.findByIdWithStudentDetail(id)
                .orElseThrow(() -> new BusinessException("Student not found with id: " + id, HttpStatus.NOT_FOUND));
        return StudentDTO.Response.fromEntity(student);
    }

    public StudentDTO.Response getStudentByStudentNumber(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new BusinessException("Student not found with student number: " + studentNumber,
                        HttpStatus.NOT_FOUND));
        return StudentDTO.Response.fromEntity(student);
    }

    @Transactional
    public StudentDTO.Response createStudent(StudentDTO.Request request) {
        // Validate student number is not already in use
        if (studentRepository.existsByStudentNumber(request.getStudentNumber())) {
            throw new BusinessException("Student already exists with student number: "
                    + request.getStudentNumber(),
                    HttpStatus.CONFLICT);
        }
        
        // Validate email is not already in use (if provided)
        if (request.getDetailRequest() != null && 
            request.getDetailRequest().getEmail() != null && 
            !request.getDetailRequest().getEmail().isEmpty() && 
            studentDetailRepository.existsByEmail(request.getDetailRequest().getEmail())) {
            throw new BusinessException("Student detail already exists with email: "
                    + request.getDetailRequest().getEmail(),
                    HttpStatus.CONFLICT);
        }

        // Validate phone number is not already in use
        if (request.getDetailRequest() != null && 
            studentDetailRepository.existsByPhoneNumber(request.getDetailRequest().getPhoneNumber())) {
            throw new BusinessException("Student detail already exists with phone number: "
                    + request.getDetailRequest().getPhoneNumber(),
                    HttpStatus.CONFLICT);
        }

        // Create student entity
        Student student = Student.builder()
                .name(request.getName())
                .studentNumber(request.getStudentNumber())
                .build();
        
        // Create student detail if provided
        if (request.getDetailRequest() != null) {
            StudentDetail studentDetail = StudentDetail.builder()
                    .address(request.getDetailRequest().getAddress())
                    .phoneNumber(request.getDetailRequest().getPhoneNumber())
                    .email(request.getDetailRequest().getEmail())
                    .dateOfBirth(request.getDetailRequest().getDateOfBirth())
                    .student(student)
                    .build();
            
            student.setStudentDetail(studentDetail);
        }

        // Save and return the student
        Student savedStudent = studentRepository.save(student);
        return StudentDTO.Response.fromEntity(savedStudent);
    }

    @Transactional
    public StudentDTO.Response updateStudent(Long id, StudentDTO.Request request) {
        // Find the student
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Student not found with id: "
                        + id, HttpStatus.NOT_FOUND));

        // Check if another student already has the student number
        if (!student.getStudentNumber().equals(request.getStudentNumber()) && 
                studentRepository.existsByStudentNumber(request.getStudentNumber())) {
            throw new BusinessException("Student already exists with student number: "
                    + request.getStudentNumber(),
                    HttpStatus.CONFLICT);
        }

        // Update student basic info
        student.setName(request.getName());
        student.setStudentNumber(request.getStudentNumber());
        
        // Update student detail if provided
        if (request.getDetailRequest() != null) {
            StudentDetail studentDetail = student.getStudentDetail();
            
            // Create new detail if not exists
            if (studentDetail == null) {
                studentDetail = new StudentDetail();
                studentDetail.setStudent(student);
                student.setStudentDetail(studentDetail);
            }
            
            // Validate email is not already in use (if changing)
            if (request.getDetailRequest().getEmail() != null && 
                !request.getDetailRequest().getEmail().isEmpty() &&
                (studentDetail.getEmail() == null || !studentDetail.getEmail().equals(request.getDetailRequest().getEmail())) &&
                studentDetailRepository.existsByEmail(request.getDetailRequest().getEmail())) {
                throw new BusinessException("Student detail already exists with email: "
                        + request.getDetailRequest().getEmail(),
                        HttpStatus.CONFLICT);
            }
            
            // Validate phone number is not already in use (if changing)
            if ((studentDetail.getPhoneNumber() == null || !studentDetail.getPhoneNumber().equals(request.getDetailRequest().getPhoneNumber())) &&
                studentDetailRepository.existsByPhoneNumber(request.getDetailRequest().getPhoneNumber())) {
                throw new BusinessException("Student detail already exists with phone number: "
                        + request.getDetailRequest().getPhoneNumber(),
                        HttpStatus.CONFLICT);
            }
            
            // Update detail fields
            studentDetail.setAddress(request.getDetailRequest().getAddress());
            studentDetail.setPhoneNumber(request.getDetailRequest().getPhoneNumber());
            studentDetail.setEmail(request.getDetailRequest().getEmail());
            studentDetail.setDateOfBirth(request.getDetailRequest().getDateOfBirth());
        }

        // Save and return updated student
        Student updatedStudent = studentRepository.save(student);
        return StudentDTO.Response.fromEntity(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new BusinessException("Student not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        studentRepository.deleteById(id);
    }
}