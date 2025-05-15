package com.basic.myspringboot;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class LambdaTest1 {

    /*
        Stream 의 map() 과 flatMap의 차이점 이해
     */
    @Test
    public void transformUsingStream(){
        List<MyCustomer> customers = List.of(
                new MyCustomer(101, "john", "john@gmail.com", Arrays.asList("397937955", "21654725")),
                new MyCustomer(102, "smith", "smith@gmail.com", Arrays.asList("89563865", "2487238947")),
                new MyCustomer(103, "peter", "peter@gmail.com", Arrays.asList("38946328654", "3286487236")),
                new MyCustomer(104, "kely", "kely@gmail.com", Arrays.asList("389246829364", "948609467"))
        );

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
        //Functional Interface가 가진 추상 메서드를 재정의할 때 람다식으로 작성하기

        // class MyRunnable implements Runnable => Thread(new MyRunnable())
        //1. Anonymous Inner Class
        // class MyRunnable implements Runnable {} - new MyRunnable()
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous Inner Class");
            }
        });
        t1.start();

        //2. Lambda Expression
        Thread t2 = new Thread(() -> System.out.println("Lambda Expression"));
        t2.start();

        //Iterable 의 forEach(Consumer consumer)
        List<String> stringList = List.of("abc", "java", "boot");
        //1. Anonymous Inner Class
        stringList.forEach(new Consumer<String>() {
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