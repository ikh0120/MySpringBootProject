package com.basic.myspringboot.service;

import com.basic.myspringboot.controller.dto.UserDTO;
import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor //final, @NonNull이 붙은 필드 생성자 자동 생성
//트랜잭션이란: 여러 작업을 하나의 작업 단위로 묶어서 모두 성공하거나 모두 실패하도록 보장하는 것
//@Transactional(readOnly = true): 읽기 전용 모드로 조회 성능 최적화 //조회할 때 데이터를 변경하지 않음
@Transactional(readOnly = true)
public class UserService {

    //@RequiredArgsConstructor로 생성자 자동 생성
    private final UserRepository userRepository;

    //등록: @Transactional(readonly = true) 해제
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //수정: @Transactional(readonly = true) 해제
    @Transactional
    public User updateUserByEmail(String email, UserDTO.UserUpdateRequest userDetail) {
        User user = getUserByEmail(email); //영속 상태
        //dirty read
        user.setName(userDetail.getName()); //변경 감지 대상
        //return userRepository.save(user);
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
    }

    //삭제: @Transactional(readonly = true) 해제
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }


}