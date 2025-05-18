package com.basic.myspringboot.auth.model;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
// 해당 Repository는 UserDetailsService(interface)를 구현한 구현체 UserInfoUserDetailsService가 존재하려면
// 우선적으로 Entity(UserInfo), Repository(UserInfoRepositoroy)가 존재해야 함
public interface UserInfoRepository extends ListCrudRepository<UserInfo, Integer> {
    //인증할 때 unique = true 설정되어있는 email 컬럼 사용
    Optional<UserInfo> findByEmail(String email);
}