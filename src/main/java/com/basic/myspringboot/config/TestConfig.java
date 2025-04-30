package com.basic.myspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public CustomVO customerVO(){
        return CustomVO.builder()  //CustomVOBuilder
                .mode("테스트환경")  //두개는 변수 넣기
                .rate(0.5)         //두개는 변수 넣기
                .build();   //bulider()로 CustomBuilder 타입으로 변경 후 값을 다 넣고 다시 CustomVO 타입으로 변경

    }
}
