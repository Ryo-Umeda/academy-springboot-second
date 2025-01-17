package com.spring.springbootapplication.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.repository.UserRepository;

import jakarta.validation.Valid;


@Service
public class UserService implements UserDetailsService{
    
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;

    // UserRepository,PasswordEncoderを注入
    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;  // パスワードのハッシュ化用
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

    // UserDetailsService インターフェースの実装
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);  //DBからユーザー情報を取得する
        if(userEntity == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません：" + email);
        }

        // 権限を付与する
        return new User(
            userEntity.getEmail(),      // ユーザー名（メールアドレス）
            userEntity.getPassword(),   // ハッシュ化されたパスワード
            Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER") // ROLEの権限を設定
            )
        );
    }
}