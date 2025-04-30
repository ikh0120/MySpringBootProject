package com.basic.myspringboot.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements ApplicationRunner { // 추상메서드 불러오기 단축키: <Ctrl> + i

    @Value("${myboot.name}")
    private String name;

    @Value("${myboot.age}")
    private int age;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("${myboot.name} = "+name);
        System.out.println("${myboot.age} = "+age);

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
