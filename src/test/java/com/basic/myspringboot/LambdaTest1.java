package com.basic.myspringboot;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class LambdaTest1 {

    /*
        Stream 의 map() 과 flatMap의 차이점 이해
     */
    @Test
    public void transformUsingStream(){
        List<MyCustomer> customers = List.of(
                // 각 MyCustomer 클래스 타입의 객체는 id, name, email, phoneNumbers를 가지고 있음
                new MyCustomer(101, "john", "john@gmail.com", Arrays.asList("397937955", "21654725")),
                new MyCustomer(102, "smith", "smith@gmail.com", Arrays.asList("89563865", "2487238947")),
                new MyCustomer(103, "peter", "peter@gmail.com", Arrays.asList("38946328654", "3286487236")),
                new MyCustomer(104, "kely", "kely@gmail.com", Arrays.asList("389246829364", "948609467"))
        );

        //MyCustomer의 이름 목록(Name List)을 추출하기: List<String>
        //  List<MyCustomer>를 List<String>으로 이름만 뽑아내고 싶음
        customers.stream() //List<MyCustomer> => Stream<MyCustomer> 변경
                // map(Function) Function의 추상 메서드 R apply(T t) //T: customer, R: customer.getName()
                .map(customer -> customer.getName()) //Stream<Mycustomer> => Stream<String> 변경
                .toList() //Stream<String> => List<String>
                .forEach(System.out::println); //for(MyCustomer c : customers.stream().map(customer -> customer.getName()).toList()) { System.out.println(c) }

        System.out.println("==============");

        //id가 103번 이상인 MyCustomer의 이름을 추출해라
        customers.stream() //List<MyCustomer> => Stream<MyCustomer> 변경
                .filter(customer -> customer.getId() >= 103) //조건 넣기
//                .map(customer -> customer.getName()) //Stream<Mycustomer> => Stream<String> 변경
                .map(MyCustomer::getName)
                .toList() //Stream<String> => List<String>
                .forEach(System.out::println);




/*
        //email 주소 목록 List<String>
        List<String> emailList = customers.stream()  //Stream<MyCustomer>
                .map(cust -> cust.getEmail()) //Stream<String>
                .collect(toList());//List<String>
        //Iterable의 forEach()
        emailList.forEach(System.out::println);

        customers.stream()
                .map(MyCustomer::getEmail)
                .collect(toList())
                .forEach(System.out::println);

        //map() : <R> Stream<R> map(Function<? super T,? extends R> mapper)
        List<List<String>> phoneList = customers.stream() //Stream<Customer>
                .map(cust -> cust.getPhoneNumbers()) //Stream<List<String>>
                .collect(toList()); //List<List<String>>
        System.out.println("phoneList = " + phoneList);


        //flatMap : <R> Stream<R> flatMap(Function<? super T,? extends Stream<? extends R>> mapper)
        List<String> phoneList2 = customers.stream() //Stream<Customer>
                .flatMap(customer -> customer.getPhoneNumbers().stream())   //Stream<String>
                .collect(toList()); //List<String>
        System.out.println("phoneList2 = " + phoneList2);
*/
    }


    /*
        java.util.function 에 제공하는 함수형 인터페이스
        Consumer -  void accept(T t)
        Predicate - boolean test(T t)
        Supplier - T get()
        Function - R apply(T t)
        Operator -
           UnaryOperator : R apply(T t)
           BinaryOperator : R apply(T t, U u)
    */



    @Test
    public void lambdaTest() {
        /** 함수형 인터페이스 Runnable 설명
         * Runnable 인터페이스는 추상 메서드인 run() 하나만 가지는 인터페이스이다
         * 자바에서는 @FunctionalInterface를 붙여 람다식으로 구현 가능하게 한다
         *     package java.lang;
         *
         *     @FunctionalInterface
         *     public interface Runnable {
         *         public abstract void run();
         *     }
         */
        /** Thread 클래스에 대하여
         * public Thread(Runnable target)
         *      Thread의 인자값으로는 Runnable 인터페이스를 구현한 구현 객체만 들어갈 수 있음
         *      Thread는 자바에서 여러 작업을 동시에 실행할 수 있도록 해주는 클래스임
         *      즉!!! 멀티스레딩(Multithreading)을 가능하게 해주는 핵심 클래스이다!!!
         */

        /** 0. 직접 클래스를 생성해서 사용하는 방법*/
        class MyRunnable implements Runnable {
            @Override
            public void run() {
                System.out.println("Make class");
            }
        }
        Thread t0 = new Thread(new MyRunnable()); //Runnable이라는 함수형 인터페이스를 구현한 구현체만 인자로 받을 수 있음
        t0.start(); //MyRunnable.run() 실행


        /**1. Anonymous Inner Class*/
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous Inner Class");
            }
        });
        t1.start();

        /**2. Lambda Expression*/
        Thread t2 = new Thread(() -> System.out.println("Lambda Expression"));
        t2.start();





        //Iterable 의 forEach(Consumer consumer)
        List<String> stringList = List.of("abc", "java", "boot");
//        System.out.println(stringList); //출력: [abc, java, boot]

        //1. Anonymous Inner Class
        stringList.forEach(new Consumer<String>() { //List<String>  타입 객체 stringList 값을 하나하나 꺼내줌
            @Override
            public void accept(String s) {
                System.out.println("s = " + s);
            }
        });

        //2. Lambda Expression
        stringList.forEach(val -> System.out.println(val));

        //3. Method Reference
        stringList.forEach(System.out::println);
    }


}