package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Customer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
//@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
//    @Disabled
    void testFindBy() {
        Optional<Customer> optionalCustomer = customerRepository.findById(1L);
        //assertThat(optionalCustomer).isNotEmpty();
        /* Optional.isPresent(): 값이 들어있으면 true 없으면 false
         * Optional.isPresent()로 값이 들어있는지 체크 후 */
        if(optionalCustomer.isPresent()){
            // Optional.get()으로 Optional 객체 안의 값을 가져옴
            Customer existCustomer = optionalCustomer.get();
            /* 가져온 객체에서 getId()로 id를 가져온 뒤 .isEqualTo()로 비교
             * 같으면 JUnit5 테스트 통과 */
            assertThat(existCustomer.getId()).isEqualTo(1L);
        }

    }

    @Test
    @Rollback(value = false) //Rollback 처리하지 말아라
    @Disabled // 해당 테스트 케이스 사용 안함
    void testCreateCustomer() throws Exception { //예외 던지기
        /**Test 작성의 핵심 구조인 Given-When-Then 패턴
         //Given(준비 단계):    테스트릴 시작하기 위해 Object나 데이터, 상태를 준비하는 단계
         //When(실행 단계):     테스트 대상이 되는 기능이나 메서드를 실제로 실행하는 단계
         //Then(검증 단계):    When 단계에서 발생한 결과가 기대한 것과 일치하는지 검증
         */

        //Given(준비 단계)
        Customer customer = new Customer();
        customer.setCustomerId("A002");
        customer.setCustomerName("스프링2");

        //When(실행 단계)
        /** customerRepository.save(customer);에서 auto_increment로 설정한 id가 증가함
         * @Rollback(value = false)가 없다면 롤백되지만 MariaDB의 auto_increment 특성상 롤백되었어도 증가된 id값은 되돌리지 않음
         */
        /* unique=true인 컬럼이 존재할 경우,
         * 동일한 값의 레코드를 삽입하면 Duplicate Error가 발생한다.
         * 이를 줄여서 Dup Error라고 부른다.
         */
        Customer addCustomer = customerRepository.save(customer);

        //Then(검증 단계)
        assertThat(addCustomer).isNotNull();
        assertThat(addCustomer.getCustomerName()).isEqualTo("스프링2");
    }
}