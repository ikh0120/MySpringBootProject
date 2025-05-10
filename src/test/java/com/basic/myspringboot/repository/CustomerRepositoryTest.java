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
@Transactional //Controller 클래스에서 사용 불가
//@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Rollback(value = false) // Rollback 하지 말고 Commit 해서 업데이트 해라
    void testUpdateCustomer(){
        /** Entity 클래스에 @DynamicUpdate 어노테이션 추가
         * @DynamicUpdate 추가하기 전:
         *      Hibernate: update customers set customer_id=?,customer_name=? where id=?
         * @DynamicUpdate 추가한 후:
         *     Hibernate: update customers set customer_name=? where id=?
         * 기본적으로 Hibernate는 모든 필드를 포함해서 UPDATE 쿼리 생성
         * @DynamicUpdate를 사용하면 UPDATE 쿼리에서 실제 변경된 컬럼만 포함시켜줌
         */
        /** 업데이트 작업 */
        // 1. 우선 findById()나 findByCustomerId()로 DB에서 읽어와야 함
        Customer customer = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));

        // 2. Setter 메서드로 값을 수정하면 @Transaction 어노테이션 때문에 즉시 값이 저장됨
        customer.setCustomerName("홍길동");

        // 3. @Transactional 어노테이션을 선언하지 않았으면 반드시 save()를 해줘야 함
            // save() 안해도 @Transactional 때문에 Update 되는 걸 "Dirty Checking"이라 함
        // customerRepository.save(customer);

        // 4. 수정되었는지 확인해본다
        assertThat(customer.getCustomerName()).isEqualTo("홍길동");

    }

    @Test
    void testByNotFoundException() { // 없을 때는 Error내서 멈추게 함
        // <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier)
        //                                      //Throwable이 RuntimeException보다 더 상위클래스
        // Supplier 인터페이스의 추상 메서드 T get()

        // 정상적이면 T 객체 반환, 없으면 RuntimeException("Customer Not Found") 반환
        Customer customer = customerRepository.findByCustomerId("A0013")
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));
//        assertThat(customer.getCustomerId()).isEqualTo("A001");
    }

    @Test
    //@Disabled
    void testFindBy() { // Optional.orElseGet(() -> T)로 주어진 값이 없을 때 빈 객체를 생성한다
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

        /**
         * 추상메서드가 하나인 인터페이스를 함수형 인터페이스라 부름
         * Interface Supplier<T>는
         * get() 메서드가 존재하고 반환값은 T(엔티티)이다
         * 함수형 인터페이스는 lambda식을 사용할 수 있다
         */
        /** Optional의 public T orElseGet(Supplier<? extends T> supplier) */
        /** Supplier 인터페이스의 추상 메서드는 T get() 하나이다. */
        // Optional<Customer> optionalCustomer2는 customer_id가 "A001"인 고객 레코드를 찾아 반환하는 메서드에서 리턴된 Optional 객체입니다.
        // 이 Optional 객체는 해당 레코드가 존재할 경우 해당 Customer 객체를 감싸고, 존재하지 않으면 빈 Optional을 가지고 있습니다.

        /* customer_id 컬럼이 "A001"인 레코드가 매핑되어 Optional<Customer> 타입으로 optionalCustomer2에 저장됨*/
        Optional<Customer> optionalCustomer2 = customerRepository.findByCustomerId("A001");

        // .orElseGet() 메서드는 Optional 객체에 값이 있을 때는 그 값을 반환하고, 값이 없을 때는 람다식을 실행하여 대체 값을 반환하는 메서드입니다.
        // orElseGet의 파라미터는 Supplier<T> 인터페이스로, T는 Customer 타입입니다. 즉, 반환 값으로 Customer 객체를 생성할 수 있는 방법을 정의해야 합니다.
        // 이 람다식 (() -> new Customer())는 Supplier<Customer> 인터페이스의 추상 메서드 get()을 구현한 것입니다.
        // Supplier의 get() 메서드는 T 타입의 객체를 반환해야 하므로, 람다식에서 new Customer()를 반환합니다.
        // 만약 Optional<Customer>가 비어있다면, 이 람다식이 실행되어 새 Customer 객체가 생성되어 반환됩니다.
        /* optionalCustomer2의 값이 Customer 객체로 존재하기에 해당 객체가 a001Customer에 저장됨  */
        /* 값이 없으면 new Customer()로 a001Customer에 저장됨  */
        Customer a001Customer = optionalCustomer2.orElseGet(() -> new Customer());
        // 따라서 optionalCustomer2가 값을 가지고 있을 경우, 해당 값을 a001Customer에 대입하고,
        // 값이 없으면 new Customer()로 새로 생성한 Customer 객체를 a001Customer에 대입하게 됩니다.
        // 이때, a001Customer는 Customer 타입의 객체로, 만약 DB에서 값이 없으면 새로 생성된 빈 Customer 객체가 들어가게 됩니다.

        // a001Customer.getCustomerName()은 lombok의 @Getter로 생성된 메서드이고,
        // optional에 값이 있었던 경우 DB의 customer_id가 "A001"인 레코드가 매핑된 것이므로
        // 그 레코드의 customer_name은 "스프링"이다
        // 따라서 assertThat("스프링").isEqualTo("스프링")이 되어 JUnit5 테스트가 통과한다
        /* a001Customer 객체의 getCustomerName()으로 꺼낸 customer_name 컬럼 값이 "스프링" 이기에 테스트 통과 */
        assertThat(a001Customer.getCustomerName()).isEqualTo("스프링");


        /**customer_id값 중 "A003"은 존재하지 않음*/
        Optional<Customer> optionalCustomer3 = customerRepository.findByCustomerId("A003");

        assertThat(optionalCustomer3).isEmpty(); // 그래서 assertThat().isEmpty() 테스트 통과
        // optionalCustomer3값이 null이면 new Customer()가 a003Cusomter에 들어가고
        // optionalCustomer3값이 null이 아니면 들어있는 값이 a003Cusomter에 들어감
        // 지금은 null이기에 a003Customer에 new Customer()가 들어감
        Customer a003Customer = optionalCustomer3.orElseGet(() -> new Customer());
        // new Customer()가 들어갔기에 null이 아님
        assertThat(a003Customer).isNotNull();
        //customer_name값을 정하지 않았기에 null
        //여기서 @Column(nullable=false)으로 null값이 들어오지 못한다고 지정했지만
        //Customer addCustomer = customerRepository.save(a003Customer);로
        // 직접 저장하지 않아 테스트 통과됨
        assertThat(a003Customer.getCustomerName()).isNull();


        // 고객 번호가 존재하는 경우
        Optional<Customer> optionalCustomer0 = customerRepository.findByCustomerId("A001");
        Customer a002Customer = optionalCustomer0.orElseGet(() -> new Customer());
        assertThat(a002Customer.getCustomerName()).isEqualTo("스프링");

        // 고객 번호가 존재하지 않는 경우
        Customer notFoundCustomer = customerRepository.findByCustomerId("A000")
                .orElseGet(() -> new Customer());
        assertThat(notFoundCustomer.getCustomerName()).isNull();


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