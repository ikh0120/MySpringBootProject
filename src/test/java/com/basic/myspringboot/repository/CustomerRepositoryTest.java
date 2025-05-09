package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Rollback(value = false)
    void testCreateCustomer() throws Exception { //예외 던지기
        //Test 작성의 핵심 구조인 Given-When-Then 패턴
        //Given(준비 단계):    테스트릴 시작하기 위해 Object나 데이터, 상태를 준비하는 단계
        //When(실행 단계):     테스트 대상이 되는 기능이나 메서드를 실제로 실행하는 단계
        //Then(검증 단계):    When 단계에서 발생한 결과가 기대한 것과 일치하는지 검증

        //Given(준비 단계)
        Customer customer = new Customer();
        customer.setCustomerId("A002");
        customer.setCustomerName("스프링2");
        //When(실행 단계)
        Customer addCustomer = customerRepository.save(customer);
        //Then(검증 단계)
        assertThat(addCustomer).isNotNull();
        assertThat(addCustomer.getCustomerName()).isEqualTo("스프링2");
    }
}