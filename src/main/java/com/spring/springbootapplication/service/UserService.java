package com.spring.springbootapplication.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.repository.UserRepository;

import jakarta.validation.Valid;

// デバック用
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;  


@Slf4j 
@Service
public class UserService implements UserDetailsService{
    
    // デバック用
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;

    // UserRepository,PasswordEncoderを注入
    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;  // パスワードのハッシュ化用
    }

    // // ユーザー登録処理
    public void registerUser(@Valid UserEntity userEntity) {
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);

        // デフォルト画像の適用（null または空の場合にセット）
        if (userEntity.getImage() == null || userEntity.getImage().length == 0) {
            try (InputStream is = getClass().getResourceAsStream("/static/images/default-profile.png")) {
                if (is != null) {
                    byte[] defaultImage = is.readAllBytes();
                    userEntity.setImage(defaultImage);

                } else {
                    throw new RuntimeException("デフォルト画像が見つかりません");
                }
            } catch (IOException e) {
                throw new RuntimeException("デフォルト画像の読み込みに失敗しました", e);
            }
        } else {
            System.out.println("ユーザーがカスタム画像をアップロード済み");
        }

        try {
            userRepository.save(userEntity);
            System.out.println("ユーザー登録完了: " + userEntity.getEmail());
        } catch (Exception e) {
            System.err.println("ユーザー登録エラー: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("ユーザー登録に失敗しました", e);
        }
    }


    // メールアドレスによるユーザー検索
    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 自己紹介の更新処理
    @Transactional
    public void updateUserBio(Long userId, String bio) {
        userRepository.updateBio(userId, bio);
    }

    // プロフィール画像の更新処理
    @Transactional
    public void updateUserProfileImage(Long userId, byte[] imageBytes) {
    if (imageBytes.length > 0) {
        userRepository.updateProfileImage(userId, imageBytes);

    } else {

        throw new IllegalArgumentException("空の画像データです");
    }
}

    // ユーザーのプロフィール画像を取得
    public byte[] getUserProfileImage(Long userId) {
        byte[] image = userRepository.findUserImageById(userId);
        if (image != null) {
            logger.info("画像取得成功、サイズ: {} bytes", image.length);
        } else {
            logger.warn("画像が見つかりません。ユーザーID {}", userId);
        }
        return image;
    }
    
    // UserDetailsService インターフェースの実装
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                return new UsernameNotFoundException("ユーザーが見つかりません：" + email);
            });

        return userEntity;
    }
}
