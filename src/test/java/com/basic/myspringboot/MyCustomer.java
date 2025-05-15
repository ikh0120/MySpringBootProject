package com.basic.myspringboot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCustomer {
    private int id;
    private String name;
    private String email;
    private List<String> phoneNumbers;

    public MyCustomer(String name, String email) {
        this.name = name;
        this.email = email;
    }
}