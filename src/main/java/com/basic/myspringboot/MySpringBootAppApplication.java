package com.basic.myspringboot; //베이스 패키지임
/* 베이스 패키지 밑이 아니면 @Component 같은거 못씀
 * 하위 패키지여야지만 사용 가능함
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/*
@SpringBootApplication =
: @SpringBootConfiguration + @EnableAutoConfiguration + @ComponentScan
   ( @Configuration )
*/
@SpringBootApplication
public class MySpringBootAppApplication {

	public static void main(String[] args) {
		//SpringApplication.run(MySpringBootAppApplication.class, args);
		SpringApplication application = new SpringApplication(MySpringBootAppApplication.class);
		//기본적으로 WebApplicationType은 웹어플리케이션(SERVLET)이다.????
		application.setWebApplicationType(WebApplicationType.SERVLET);
		application.run(args);

	}

	@Bean
	public String hello(){
		return "hello springboot";
	}

}
