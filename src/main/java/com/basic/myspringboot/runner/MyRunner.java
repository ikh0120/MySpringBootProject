package com.basic.myspringboot.runner;

import com.basic.myspringboot.config.CustomVO;
import com.basic.myspringboot.property.MyBootProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements ApplicationRunner { // 추상메서드 불러오기 단축키: <Ctrl> + i

    // 환경변수를 @Value에 넣는데변수 이름에 공백 혹은 대소문자 지켜 넣기
    @Value("${myboot.name}")
    private String name;

    @Value("${myboot.age}")
    private int age;

    @Autowired
    private Environment environment;

    @Autowired //@Component가 있기에 쓸 수 있음
    private MyBootProperties properties;

    @Autowired
    private CustomVO customVO;

    // import org.slf4j.Logger, org.slf4j.LoggerFactory
    private Logger logger = LoggerFactory.getLogger(MyRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Logger 구현체 = " + logger.getClass().getName());

        /** logger.info가 레벨이 더 상위여서
         * spring.profile.active=prod로 설정하면
         * logger.debug가 안나옴*/
        logger.debug("${myboot.name} = {}", name);
        logger.debug("${myboot.age} = {}", age);
        //Environment.getProperty("변수") : 변수는 환경변수를 가져올 때 대소문자 구분을 안하지만 변수 이름은 구분함
        logger.debug("${myboot.fullName} = {}", environment.getProperty("myboot.fullName"));

       logger.info("\n");
       logger.info("MyBootProperties getName() = {}", properties.getName());
       logger.info("MyBootProperties getAge() = {}", properties.getAge());
       logger.info("MyBootProperties getFullName() = {}", properties.getFullName());
       logger.info("설정된 Port 번호 = {}", environment.getProperty("local.server.port"));
       logger.info("\n");

        //java -jar -Dserver.port=8088 .\target\MySpringBootApp-0.0.1-SNAPSHOT.jar --myboot.name=스프링 --spring.profiles.active=prod
        // 이 명령어로 실행하면 --spring.profiles.active 옵션으로
        // applicaiton.properties보다 우선순위가 높기에
        // --spring.profiles.active=prod로 설정되어 test 빈이 비활설화, prod빈이 활성화되어 실행됨
        logger.info("활성화된 CustomVO Bean: {}", customVO);

        // foo 라는 VM 아규먼트가 있는지 확인하기
        logger.debug("VM 아규먼트 foo : {}", args.containsOption("foo"));
        // bar 라는 Program 아규먼트가 있는지 확인하기
        logger.debug("Program 아규먼트 bar : {}", args.containsOption("bar"));

        /*
            Iterable forEach(Consumer)
            Cunsumer 는 함수형 인터페이스 void accept(T t)
            Consumer 의 추상 메서드를 오버라이딩 하는 구문을 람다식으로 작성하자
         */

        //Program 아규먼트 목록 출력
        args.getOptionNames()  //Set<String>
                .forEach(name -> System.out.println(name));
    }
}
