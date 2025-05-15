package com.basic.myspringboot.exception.advice;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data // Lombok이 getter, setter, toString, equals 등을 자동 생성
public class ErrorObject {
    private Integer statusCode; // HTTP 상태 코드 (예: 400, 500 등)
    private String message;     // 에러 메시지
    private String timestamp;   // 타임스탬프 (에러 발생 시간)

    public String getTimestamp() {
        LocalDateTime ldt = LocalDateTime.now(); // 현재 시간 구함

        return DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss E a", // 날짜 형식 지정: 년-월-일 시:분:초 요일 오전/오후
                Locale.KOREAN).format(ldt); // 한국어로 포맷 적용
    }
}
