package com.basic.myspringboot.controller.dto;

import com.basic.myspringboot.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserDTO {
    
    @Getter
    @Setter
    public static class UserCreateRequest {
        @NotEmpty(message = "Name 은 필수 입력항목입니다.")
        private String name;
        
        @NotBlank(message = "Email 은 필수 입력항목입니다.")
        private String email;
    }
    
    @Getter
    @Setter
    public static class UserUpdateRequest {
        @NotEmpty(message = "Name 은 필수 입력항목입니다.")
        private String name;
    }
    
    @Getter
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
        private LocalDateTime createdAt;
        
        public UserResponse(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt();
        }
    }
}