package com.spring.springbootapplication.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {
    
    private final UserRepository userRepository ;
    private final BCryptPasswordEncoder passwordEncoder;

    // UserRepositoryを注入
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();  // パスワードのハッシュ化用
    }


    // ユーザー登録処理
    public void registerUser(@Valid UserEntity userEntity) {
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
    }

    // メールアドレスによるユーザー検索
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}