package com.basic.myspringboot.config;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString @Builder
public class CustomVO {
    private String mode;
    private double rate;
}
