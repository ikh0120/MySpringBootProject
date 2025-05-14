package com.basic.myspringboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override /** resources/mobile/index.html 인식시켜주는 메서드 */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/mobile/**")
                //반드시 mobile 다음에 / 을 주어야 한다.
                .addResourceLocations("classpath:/mobile/") // classpath는 target/classes을 의미
                .setCachePeriod(20);//20초
    }
}