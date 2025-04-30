package com.basic.myspringboot.runner;

import com.basic.myspringboot.property.MyBootProperties;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("${myboot.name} = " + name);
        System.out.println("${myboot.age} = " + age);
        //Environment.getProperty("변수") : 변수는 환경변수를 가져올 때 대소문자 구분을 안하지만 변수 이름은 구분함
        System.out.println("${myboot.fullName} = " + environment.getProperty("myboot.fullName"));

        System.out.println();
        System.out.println("MyBootProperties getName() = " + properties.getName());
        System.out.println("MyBootProperties getAge() = " + properties.getAge());
        System.out.println("MyBootProperties getFullName() = " + properties.getFullName());
        System.out.println();
        System.out.println("설정된 Port 번호 = "+environment.getProperty("local.server.port"));
        // foo 라는 VM 아규먼트가 있는지 확인하기
        System.out.println("VM 아규먼트 foo : " + args.containsOption("foo"));
        // bar 라는 Program 아규먼트가 있는지 확인하기
        System.out.println("Program 아규먼트 bar : " + args.containsOption("bar"));

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
