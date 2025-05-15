package com.basic.myspringboot.exception.advice;

import com.basic.myspringboot.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/** @NotBlank(입력항목을 처리하는 어노테이션)에서 오류가 생길 때 처리하기 위한 핸들러 추가*/

@RestControllerAdvice // 모든 컨트롤러 예외를 처리하는 클래스
public class GlobalExceptionHandler {

    /** 사용자 정의 예외인 BusinessException 처리 */
    @ExceptionHandler(BusinessException.class) //BusinessException 처리
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(), // 상태코드
                ex.getMessage(),            // 메시지
                LocalDateTime.now()         // 발생 시간
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus()); // 응답 반환
    }

    //@Valid 혹은 @NotBlank 등 입력 항목 검증할때 오류 발생 시 동작하는 메서드
    @ExceptionHandler(MethodArgumentNotValidException.class) //MethodArgumentNotValidException 처리
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        //@Valid 또는 @NotBlank 등 유효성 검사를 실패했을 때 발생하는 예외를 처리
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
            // 검증 실패한 필드의 오류 정보를 하나씩 가져옴
            String fieldName = ((FieldError) error).getField(); // 필드(변수) 이름 추출
            String errorMessage = error.getDefaultMessage(); // 에러 메시지(@NotBlank의 message) 추출
            errors.put(fieldName, errorMessage); // 필드명과 메시지를 map에 저장
        });

        ValidationErrorResponse response = new ValidationErrorResponse(
                400,
                "입력 항목 검증 오류",
                LocalDateTime.now(),
                errors
        );
        //badRequests(): 400
        return ResponseEntity.badRequest().body(response); // 400 상태로 응답
    }

//    바로 위 handleValidationExceptions에서 저장한 내용들을 전부 집어넣어 반환
    @Getter @Setter
    @AllArgsConstructor
    public static class ValidationErrorResponse {
        private int status;              // 상태 코드
        private String message;          // 메시지
        private LocalDateTime timestamp; // 발생 시간
        private Map<String, String> errors; // 검증 실패한 필드별 메시지
    }

    /***내부 클래스 정의*/
    @Getter @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private int status;              // 상태 코드
        private String message;          // 메시지
        private LocalDateTime timestamp; // 발생 시간
    }


}