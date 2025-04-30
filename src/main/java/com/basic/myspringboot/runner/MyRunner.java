package com.basic.myspringboot.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements ApplicationRunner { // 추상메서드 불러오기 단축키: <Ctrl> + i

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
