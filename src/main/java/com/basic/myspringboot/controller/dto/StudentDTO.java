package com.basic.myspringboot.controller.dto;

import com.basic.myspringboot.entity.Student;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//StudentDTO
public class StudentDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Student name is required")
        @Size(max = 100, message = "Student name cannot exceed 100 characters")
        private String name;

        @NotBlank(message = "Student number is required")
        @Size(max = 20, message = "Student number cannot exceed 20 characters")
        private String studentNumber;
        
        @Valid
        private StudentDetailDTO detailRequest;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentDetailDTO {
        @NotBlank(message = "Address is required")
        @Size(max = 200, message = "Address cannot exceed 200 characters")
        private String address;
        
        @NotBlank(message = "Phone number is required")
        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        private String phoneNumber;
        
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        private String email;
        
        private LocalDate dateOfBirth;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String studentNumber;
        private StudentDetailResponse detail;

        public static Response fromEntity(Student student) {
            StudentDetailResponse detailResponse = student.getStudentDetail() != null
                    ? StudentDetailResponse.builder()
                        .id(student.getStudentDetail().getId())
                        .address(student.getStudentDetail().getAddress())
                        .phoneNumber(student.getStudentDetail().getPhoneNumber())
                        .email(student.getStudentDetail().getEmail())
                        .dateOfBirth(student.getStudentDetail().getDateOfBirth())
                        .build()
                    : null;
            
            return Response.builder()
                    .id(student.getId())
                    .name(student.getName())
                    .studentNumber(student.getStudentNumber())
                    .detail(detailResponse)
                    .build();
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentDetailResponse {
        private Long id;
        private String address;
        private String phoneNumber;
        private String email;
        private LocalDate dateOfBirth;
    }
}