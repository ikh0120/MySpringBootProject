package com.basic.myspringboot.exception.advice;

import com.basic.myspringboot.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 잡는 역할 (AOP)
@Slf4j // 로그 기록을 위한 Lombok 어노테이션
public class DefaultExceptionAdvice {

    /** BusinessException이 발생했을 때 처리하는 핸들러 */
    //@ExceptionHandler(BusinessException.class)
    //public ResponseEntity<ErrorObject> handleResourceNotFoundException(BusinessException ex) {
    //    ErrorObject errorObject = new ErrorObject(); // 에러 정보 담을 객체 생성
    //    errorObject.setStatusCode(ex.getHttpStatus().value()); // 상태 코드 설정
    //    errorObject.setMessage(ex.getMessage()); // 에러 메시지 설정
    //
    //    log.error(ex.getMessage(), ex); // 로그에 에러 메시지 기록
    //
    //    return new ResponseEntity<ErrorObject>(errorObject, HttpStatusCode.valueOf(ex.getHttpStatus().value()));
    //}

    /*
        Spring6 버전에 추가된 ProblemDetail 객체에 에러정보를 담아서 리턴하는 방법
     */
    //@ExceptionHandler(BusinessException.class)
    //protected ProblemDetail handleException(BusinessException e) {
    //    ProblemDetail problemDetail = ProblemDetail.forStatus(e.getHttpStatus());
    //    problemDetail.setTitle("Not Found");
    //    problemDetail.setDetail(e.getMessage());
    //    problemDetail.setProperty("errorCategory", "Generic");
    //    problemDetail.setProperty("timestamp", Instant.now());
    //    return problemDetail;
    //}

    //숫자타입의 값에 문자열타입의 값을 입력으로 받았을때 발생하는 오류

    /** 숫자 필드에 문자열이 들어오거나 JSON 파싱 에러 등이 발생했을 때 처리 */
    @ExceptionHandler(HttpMessageNotReadableException.class) // HttpMessageNotReadableException 처리
    protected ResponseEntity<Object> handleException(HttpMessageNotReadableException e) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("message", e.getMessage()); // 에러 메시지 저장
        result.put("httpStatus", HttpStatus.BAD_REQUEST.value()); // 400 상태 코드 저장

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST); // 400 응답 반환
    }


    /** 런타임 예외가 발생했을 때 처리 */
    @ExceptionHandler(RuntimeException.class) //RuntimeException 처리
    protected ResponseEntity<ErrorObject> handleException(RuntimeException e) {
        ErrorObject errorObject = new ErrorObject(); // 에러 정보 객체 생성
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500 설정
        errorObject.setMessage(e.getMessage()); // 예외 메시지

        log.error(e.getMessage(), e); // 로그 출력

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatusCode.valueOf(500));
    }
}
