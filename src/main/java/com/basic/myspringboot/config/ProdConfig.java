package com.basic.myspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// 이거로 ProdConfig, TestConfig의 같은 이름의 빈을
// application.properties에서 구분해줌
@Profile("prod")
@Configuration
public class ProdConfig {
    @Bean
    public CustomVO customerVO(){
        return CustomVO.builder()  //CustomVOBuilder
                .mode("운영환경")  //두개는 변수 넣기
                .rate(0.5)         //두개는 변수 넣기
                .build();   //bulider()로 CustomBuilder 타입으로 변경 후 값을 다 넣고 다시 CustomVO 타입으로 변경

    }
}
