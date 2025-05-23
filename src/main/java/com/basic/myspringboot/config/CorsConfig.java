package com.basic.myspringboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/***
 * ✅ SOP(Same-Origin Policy)
 * - 브라우저 보안 정책 중 하나로, 같은 출처(origin)에서 로드된 웹 페이지만 그 출처의 리소스에 접근할 수 있도록 제한한다.
 * - 출처(origin)는 프로토콜 + 도메인 + 포트 번호로 구성된다.
 *   예: http://localhost:3000 과 http://localhost:8080 은 다른 출처이다.
 *
 * ✅ CORS(Cross-Origin Resource Sharing)
 * - 서로 다른 출처 간에 리소스를 주고받을 수 있도록 브라우저에서 허용하는 표준.
 * - 서버가 `Access-Control-Allow-Origin` 등의 HTTP 헤더를 통해 허용할 출처를 명시해야 함.
 * - 이를 통해 SOP의 제한을 우회할 수 있게 된다 (보안은 유지하면서).
 *
 * ✅ Origin (출처)
 * - 브라우저가 요청을 보낸 곳의 주소를 의미.
 * - 구성: [프로토콜]://[호스트]:[포트]
 *   예) http://example.com:8080
 *
 * ✅ @CrossOrigin (스프링 어노테이션)
 * - 스프링 컨트롤러 또는 메서드에서 CORS를 허용하고 싶을 때 사용.
 * - 예: @CrossOrigin(origins = "http://localhost:5050")
 * - 클래스 레벨 또는 메서드 레벨에 선언 가능.
 *
 * ✅ WebMvcConfigurer (스프링 인터페이스)
 * - WebMvc 설정을 커스터마이징할 수 있는 인터페이스.
 * - addCorsMappings() 메서드를 오버라이드하여 글로벌 CORS 정책 설정 가능.
 *   예: 특정 경로에 대해 어떤 origin, method를 허용할지 설정 가능.
 ***/
@Configuration // 스프링 설정 클래스임을 나타냄
public class CorsConfig {
    @Bean
    public FilterRegistrationBean<?> corsConfigurationSource() {

        // CORS 정책을 담는 객체 생성
        CorsConfiguration configuration = new CorsConfiguration();

        // 모든 출처(Origin)에 대해 허용 (예: http://localhost:5500 등)
        configuration.setAllowedOriginPatterns(List.of("*"));

        // 쿠키, 인증 정보 포함 요청 허용
        configuration.setAllowCredentials(true);

        // 허용할 요청 헤더 설정
        configuration.setAllowedHeaders(Arrays.asList(
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "Origin","Cache-Control", "Content-Type", "Authorization"));

        // 허용할 HTTP 메서드 설정
        configuration.setAllowedMethods(Arrays.asList("POST", "DELETE", "GET", "PATCH", "PUT"));

        // 설정한 CORS 정책을 URL 패턴에 매핑
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로에 대해 CORS 설정 적용
        source.registerCorsConfiguration("/**", configuration);

        // CORS 필터를 스프링 필터 체인에 등록
        FilterRegistrationBean<?> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        // 필터의 우선 순위 설정 (숫자가 낮을수록 먼저 실행)
        bean.setOrder(0);

        return bean;
    }
}