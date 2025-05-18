package com.basic.myspringboot.auth.model;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
// 해당 Repository는 UserDetailsService<<implements>> -- UserInfoUserDetailsService -- UserInfoRepositoroy -- UserInfo 순서로 연결되어 있음
public interface UserInfoRepository extends ListCrudRepository<UserInfo, Integer> {
    //인증할 때 unique = true 설정되어있는 email 컬럼 사용
    Optional<UserInfo> findByEmail(String email);
}